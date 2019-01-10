package com.buahbatu.januar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_firebase.*

class FirebaseActivity : AppCompatActivity() {

    // Write a message to the database
    private val database by lazy { FirebaseDatabase.getInstance() }
    private val myRef by lazy { database.getReference("message") }

    fun onClick() {
        myRef.setValue("Hello, World!")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase)

        FirebaseApp.initializeApp(this)

        btnFirebase.setOnClickListener {
            onClick()
        }
    }
}
