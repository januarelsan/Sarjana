package com.buahbatu.januar

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    private val fragmentList = listOf(
        GraphFragment(), DataFragment()
    )

    var isLampOn = false

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame, fragmentList[0])
                    commit()
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame, fragmentList[1])
                    commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        btnSwitch.setOnClickListener {
            isLampOn = !isLampOn
            Toast.makeText(this, if (isLampOn) "Lampu Menyala" else "Lampu Mati", Toast.LENGTH_SHORT).show()
            btnSwitch.supportImageTintList = if (isLampOn) {
                ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.colorLight))
            } else {
                null
            }
        }
    }
}
