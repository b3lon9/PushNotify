package kr.co.cavedwellers.notificationapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.b3lon9.nlog.LogLevel
import com.b3lon9.nlog.NLog

class MainActivity : AppCompatActivity() {
    private lateinit var channelId: String      // 채널 아이디
    private lateinit var channelName: String    // 채널 이름

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
    }

    private fun initChannelInfo() {
        channelId = this.resources.getString(R.string.notify_channel_id)
        channelName = this.resources.getString(R.string.notify_channel_name)
    }

    // NotifiactionCompat.Builder 기본 구성
    private fun getBuilderDefault(context: Context, channelId: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("NotificationApp Title")
            .setContentText("NotificationApp Text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)       // Android 8.0 이하만 설정
    }

    // 채널 등록 Android 8.0이상부터 필수
    // register NotificationChannel
    // NotificationManager.createNotificationChannel(channel)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * permission
     * */
    // 단일 권한 : RequestPermission()
    // 여러 권한 : RequestMultiplePermission()
    private val requestPermissionLauncher = registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
        NLog.v("requestPermissionLauncher.. isGranted:$isGranted")
        if (isGranted) {
            initChannelInfo()
            createNotificationChannel()
        } else {

        }
    }
    private fun checkPermission() {
        when {
            // 권한이 수락되어 있는지
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                NLog.v("checkPermission.. checkSelfPermission Granted")
                initChannelInfo()
                createNotificationChannel()
            }
            // 사용자에게 왜 이 권한이 필요한지 설명
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS) -> {
                Toast.makeText(this, "권한 설정해야해요", Toast.LENGTH_SHORT).show()
                NLog.v("checkPermission.. shouldShowRequestPermissionRationale..")
            } else -> {
                NLog.v("checkPermission.. cancel..")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 12345)
                }
            }
        }
    }

    // Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
    // 왜냐하면 requestPermissionLauncher에서 허용/거부를 체크하기 때문에
    // 이중으로 onRequestPermissionResult에서 체크해줄 필요가 없기 때문이다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            NLog.l(LogLevel.VERBOSE)
            NLog.v("onRequestPermissionResult.. requestCode:$requestCode")
            NLog.v("onRequestPermissionResult.. permissions:$permissions")
            NLog.v("onRequestPermissionResult.. grantResults:$grantResults")
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                NLog.v("onRequestPermissionResult.. init..!!")
                initChannelInfo()
                createNotificationChannel()
            }
        }
    }
}