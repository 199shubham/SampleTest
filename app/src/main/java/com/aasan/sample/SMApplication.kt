package com.aasan.sample

import com.globalmed.corelib.base.BaseApplication
import com.globalmed.corelib.base.NetworkErrorListener
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SMApplication: BaseApplication(), NetworkErrorListener {
    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
    }

    override fun onApiNotFound() {

    }

    override fun onDomainCouldNotReach() {
    }

    override fun logout() {
    }
}