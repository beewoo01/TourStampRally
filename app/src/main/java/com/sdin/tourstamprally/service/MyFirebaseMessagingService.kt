package com.sdin.tourstamprally.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.wtf("MyFirebaseMessagingService", "onNewToken = $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.wtf("MyFirebaseMessagingService", "onMessageReceived = $p0")
        super.onMessageReceived(p0)
    }
}