package kr.co.cavedwellers.fingertestapp

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import com.b3lon9.nlog.NLog
import com.fingerpush.android.FingerNotification
import com.fingerpush.android.FingerPushFcmListener

class IntentService : FingerPushFcmListener() {

    override fun onCreate() {
        super.onCreate()
        NLog.v("onCreate..")
    }

    override fun onMessage(context: Context?, data: Bundle?) {
        NLog.v("onMessage.......data:$data")
        createNotification(data!!)
    }

    private fun createNotification(data: Bundle) {
        val intent = Intent(this@IntentService, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this@IntentService, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getActivity(this@IntentService, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
        }

        val fingerNotification: FingerNotification = FingerNotification(this@IntentService)
        fingerNotification.apply {
            setNotificationIdentifier(System.currentTimeMillis().toInt())
            setIcon(R.drawable.ic_launcher_foreground)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            setVibrate(longArrayOf(0, 500, 600, 1000))
            setLights(Color.parseColor("#ffff00ff"), 500, 500)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fingerNotification.setColor(Color.rgb(0, 114, 162))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = this.resources.getString(R.string.default_notification_channel_id)
            val channelName = this.resources.getString(R.string.default_notification_channel_name)

            fingerNotification.createChannel(channelId, channelName)
        }

        fingerNotification.showNotification(data, pendingIntent)
    }

   /* 기본 notification 로직
   @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(data: Bundle) {
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // API 26이상 Channel지정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = this.resources.getString(R.string.default_notification_channel_id)
            val channelName = this.resources.getString(R.string.default_notification_channel_name)

            val channel: NotificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = "FingerPush Test Channel"
            notificationManager.createNotificationChannel(channel)
        }

        // send
        val intent = Intent(this@IntentService, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        // pending
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this@IntentService, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getActivity(this@IntentService, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        val builder: NotificationCompat.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = this.resources.getString(R.string.default_notification_channel_id)
            NotificationCompat.Builder(this@IntentService, channelId)
        } else {
            NotificationCompat.Builder(this@IntentService)
        }
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(data.getString("data.title"))
            .setContentText(data.getString("data.message"))

        builder.setContentIntent(pendingIntent)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }*/
}