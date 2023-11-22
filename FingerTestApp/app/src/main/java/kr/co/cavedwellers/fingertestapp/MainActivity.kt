package kr.co.cavedwellers.fingertestapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.b3lon9.nlog.NLog
import com.fingerpush.android.FingerPushFcmListener
import com.fingerpush.android.FingerPushManager
import com.fingerpush.android.NetworkUtility
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askNotificationPermission()


        // Android 8(API Lv26)이상 부터 Channel을 생성해야 알람이 노출됨
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = this.resources.getString(R.string.default_notification_channel_id)
            val channelName = this.resources.getString(R.string.default_notification_channel_name)
        }
    }

    // setDevice()
    private fun initFingerPush() {
        FingerPushManager.getInstance(this@MainActivity).setDevice(object: NetworkUtility.ObjectListener {
            override fun onComplete(p0: String?, p1: String?, p2: JSONObject?) {
                NLog.d("onComplete.. p0:$p0")
                NLog.w("==================")
                NLog.d("onComplete.. p1:$p1")
                NLog.w("==================")
                NLog.d("onComplete.. p2:$p2")
                // code 200, 201 단말기 등록

                // 식별자 등록
                FingerPushManager.getInstance(this@MainActivity).setIdentity("kgeduone", object: NetworkUtility.ObjectListener {
                    override fun onComplete(p0: String?, p1: String?, p2: JSONObject?) {
                        NLog.d("onComplete.. p0:$p0")
                        NLog.w("==================")
                        NLog.d("onComplete.. p1:$p1")
                        NLog.w("==================")
                        NLog.d("onComplete.. p2:$p2")
                    }

                    override fun onError(p0: String?, p1: String?) {
                        NLog.e("onError.. p0:$p0")
                        NLog.w("================")
                        NLog.e("onError.. p1:$p1")
                    }

                })
            }

            override fun onError(p0: String?, p1: String?) {
                NLog.e("onError.. p0:$p0")
                NLog.w("================")
                NLog.e("onError.. p1:$p1")
                // code 504 단말기 이미 등록?

                // 식별자 등록
                FingerPushManager.getInstance(this@MainActivity).setIdentity("kgeduone", object: NetworkUtility.ObjectListener {
                    override fun onComplete(p0: String?, p1: String?, p2: JSONObject?) {
                        NLog.d("onComplete.. p0:$p0")
                        NLog.w("==================")
                        NLog.d("onComplete.. p1:$p1")
                        NLog.w("==================")
                        NLog.d("onComplete.. p2:$p2")
                    }

                    override fun onError(p0: String?, p1: String?) {
                        NLog.e("onError.. p0:$p0")
                        NLog.w("================")
                        NLog.e("onError.. p1:$p1")
                    }

                })
            }

        })
    }
    
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            initFingerPush()
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                initFingerPush()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            initFingerPush()
        }
    }
}