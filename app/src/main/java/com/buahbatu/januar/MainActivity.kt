package com.buahbatu.januar

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.buahbatu.januar.kmeans.Algo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : FragmentActivity() {
    companion object {
        const val SERVER_URI = "tcp://m15.cloudmqtt.com:19743"
        const val PUBLISH_TOPIC = "lampu"
        const val USERNAME = "gocfayyc"
        val PASSWORD = "QYxF48v4Gh6C".toCharArray()
        var CLIENT_ID = "android-januar"
    }

    private val database by lazy { FirebaseDatabase.getInstance().getReference("lamp_data") }

    private var selectedFragment = 0

    private var itemCurrent = LampModel(false, "")

    private val mqttClient by lazy {
        MqttAndroidClient(this, SERVER_URI, CLIENT_ID)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                selectedFragment = 0
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame, GraphFragment())
                    commit()
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_dashboard -> {
                selectedFragment = 1
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame, DataFragment())
                    commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun Date.toSimpleString() : String {
        val format = SimpleDateFormat(TimeFormat, Locale.getDefault())
        return format.format(this)
    }

    private fun createPayload(): String {
        return itemCurrent.toPayload()
    }

    private fun publishMessage() {
        if (mqttClient.isConnected) {
            try {
                mqttClient.publish(PUBLISH_TOPIC, MqttMessage().apply {
                    payload = createPayload().toByteArray()
                    isRetained = true
                })
            } catch (e: MqttException) {
                System.err.println("JANUAR: Error Publishing: " + e.message)
                e.printStackTrace()
            }
        } else {
            println("JANUAR: MQTT is not connected yet")
        }
    }

    private fun saveData() {
        // Persist your data to firebase
        database.push().setValue(itemCurrent)
    }

    fun subscribeMessage() {
        if (mqttClient.isConnected) {
            mqttClient.subscribe(PUBLISH_TOPIC, 0) { _, message ->
                val data = LampModel.fromPayload(message.toString())
                Data.itemList.add(data)
                itemCurrent = data

                saveData()
                updateButton()
                navigation.selectedItemId = selectedFragment
            }
        } else {
            println("JANUAR: MQTT is not connected yet")
        }
    }

    private fun setupMqtt () {
        try {
            mqttClient.connect(MqttConnectOptions().apply {
                userName = USERNAME
                password = PASSWORD
                isAutomaticReconnect = true
                isCleanSession = false
            }, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    println("JANUAR: onSuccess: ${asyncActionToken.toString()}")
                    subscribeMessage()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    println("JANUAR: Failed to connect to: $SERVER_URI")
                }
            })
        } catch (e: MqttException) {
            System.err.println("JANUAR: Error connect: " + e.message)
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        if (mqttClient.isConnected) mqttClient.disconnect()
        mqttClient.unregisterResources()
        super.onDestroy()
    }

    fun updateButton() {
        btnSwitch.supportImageTintList = if (itemCurrent.isLampOn) {
            ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.colorLight))
        } else {
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.apply {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_home
        }

        setupMqtt()

        btnSwitch.setOnClickListener {
            itemCurrent = LampModel(!itemCurrent.isLampOn, Calendar.getInstance().time.toSimpleString())

            Toast.makeText(this, if (itemCurrent.isLampOn) "Lampu Menyala" else "Lampu Mati", Toast.LENGTH_SHORT).show()
            updateButton()

            publishMessage()
        }

        getFirebaseData()
    }

    private fun getFirebaseData() {
        // get data from firebase
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(MainActivity::class.java.simpleName, "runAlgo", databaseError.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lampData = dataSnapshot.children.mapNotNull { it.getValue(LampModel::class.java) }
                runAlgo(lampData)
            }
        })
    }

    private fun runAlgo(lampData: List<LampModel>) {
        if (lampData.isEmpty()) {
            Snackbar
                .make(btnSwitch, "Data belum cukup untuk rekomendasi", Snackbar.LENGTH_SHORT)
                .show()
            return
        }

        val clusters = Algo(lampData).run()

        Calendar.getInstance().run {
            val hours = get(Calendar.HOUR_OF_DAY) * 3600.0 // to double
            val minutes = get(Calendar.MINUTE) * 60.0 // to double
            val seconds = get(Calendar.SECOND) * 1.0 // to double

            val total = hours + minutes + seconds

            clusters.firstOrNull { it.x <= total }?.let {
                val shouldOn = if(it.y >= 0.5) "menyalakan" else "matikan"
                Snackbar.make(btnSwitch, "Anda disarankan untuk $shouldOn lampu", Snackbar.LENGTH_INDEFINITE).apply {
                    setAction("OK") {
                        dismiss()
                    }

                    show()
                }
            } ?:
            Snackbar
                .make(btnSwitch, "Data belum cukup untuk rekomendasi", Snackbar.LENGTH_SHORT)
                .show()

        }
    }
}
