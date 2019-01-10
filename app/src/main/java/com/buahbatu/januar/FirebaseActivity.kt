package com.buahbatu.januar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_firebase.*

class FirebaseActivity : AppCompatActivity() {

    private val database by lazy { FirebaseDatabase.getInstance().getReference("lamp_data") }

    fun onClick() {
        database.push().setValue(LampModel(isLampOn = false, time = "besok"))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase)

        btnFirebase.setOnClickListener {
            onClick()
        }
    }
}
