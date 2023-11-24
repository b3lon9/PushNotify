package kr.co.cavedwellers.firebasecloudmessagingapp.serivce

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.b3lon9.nlog.LogLevel
import com.b3lon9.nlog.NLog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.co.cavedwellers.firebasecloudmessagingapp.MainActivity
import kr.co.cavedwellers.firebasecloudmessagingapp.R

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
        NLog.l(level = LogLevel.VERBOSE, count = 20)
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
        NLog.v("onMessageReceived.. message:${message.notification?.title}")
        NLog.v("onMessageReceived.. message:${message.notification?.body}")
        sendNotification(message.notification?.title!!, message.notification?.body!!)
    }

    /**
     * 경우에 따라 FCM에서 메시지를 전송하지 못하는 경우도 있다.
     * 특정 기기가 대기 중인 메시지가 너무 많거나(100개 이상), 기기가 한 달 넘게 FCM에 연결되지 않으면 문제가 발생
     * -- 이러한 경우 [onDeletedMessages]에 대한 콜백이 수신된다.
     *
     * 이 콜백을 수신한 앱 인스턴스는 앱 서버와 전체 동기화를 수행해야 한다.
     * */
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    private fun sendNotification(titleMsg: String, contentMsg: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(titleMsg)
            .setContentText(contentMsg)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // since android Oreo API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}