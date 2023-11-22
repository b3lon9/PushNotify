package kr.co.cavedwellers.fingertestapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.b3lon9.nlog.NLog
import com.fingerpush.android.FingerPushManager
import com.fingerpush.android.NetworkUtility
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askNotificationPermission()
    }


    private fun initFingerPush() {
        FingerPushManager.getInstance(this@MainActivity).setDevice(object: NetworkUtility.ObjectListener {
            override fun onComplete(p0: String?, p1: String?, p2: JSONObject?) {
                NLog.d("onComplete..")
                // code 200, 201 단말기 등록
            }

            override fun onError(p0: String?, p1: String?) {
                NLog.e("onError..")
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