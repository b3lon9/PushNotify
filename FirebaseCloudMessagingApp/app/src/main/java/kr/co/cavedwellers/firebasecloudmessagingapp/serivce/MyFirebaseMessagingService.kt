package kr.co.cavedwellers.firebasecloudmessagingapp.serivce

import com.b3lon9.nlog.NLog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
        NLog.v("onCreate..")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        NLog.d("onNewToken.. token:$token")
    }

    private fun sendRegistrationToServer(token: String) {

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        NLog.v("onMessageReceived.. message:${message}")
        NLog.v("onMessageReceived.. message:${message.data}")
    }
}