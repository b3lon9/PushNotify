package kr.co.cavedwellers.notificationapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.b3lon9.nlog.NLog
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * 한글과 영문의 만남English and Korean met만남
 * 한글English한글English
 * */
class MainActivity : AppCompatActivity() {
    private lateinit var channelId: String
    private lateinit var channelName: String

    private lateinit var builder: NotificationCompat.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        channelId = this.resources.getString(R.string.notify_channel_id)
        channelName = this.resources.getString(R.string.notify_channel_name)

        // request permissions
        if (allPermissionsGranted()) {
            // Permission Granted
            createNotificationChannel()
        } else {
            // Permission Rejected
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 123)
        }
        // Android 8.0(API:26) 이하(Android 7.1)는 Priority로 우선순위를 정한다
        // Android 8.0(API:26) 이상부터 ChannelID, ChannelName식별자를 주고,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = NotificationCompat.Builder(this@MainActivity, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("content")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        } else {

        }

    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = this.resources.getString(R.string.notify_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel: NotificationChannel = NotificationChannel(channelId, channelName, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    public fun notify(v: View) {
        NLog.l()
        NLog.d("notify..")
        with(NotificationManagerCompat.from(this@MainActivity)) {
            notify(Constants.NotifyID, builder.build())
        }
    }


    // 요청할 권한 목록
    private val REQUIRED_PERMISSIONS: Array<String> = mutableListOf(
        Manifest.permission.POST_NOTIFICATIONS
    ).apply {
        // 해당 버전에 맞게 설정하는 부분
    }.toTypedArray()

    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}