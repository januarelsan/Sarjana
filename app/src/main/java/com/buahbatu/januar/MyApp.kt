package com.buahbatu.januar

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration



class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val mRealmConfiguration = RealmConfiguration.Builder()
            .name("januar.realm")
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.getInstance(mRealmConfiguration)
        Realm.setDefaultConfiguration(mRealmConfiguration)
    }
}