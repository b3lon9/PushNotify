package kr.co.cavedwellers.firebasecloudmessagingapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.b3lon9.nlog.NLog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // FCM이 자동 초기화 설정
        // Firebase.messaging.isAutoInitEnabled = true      // 다시 자동 초기화됨

        askNotificationPermission()
        checkToken()
    }

    override fun onResume() {
        super.onResume()

        // Google Play 서비스 확인
    }

    // 현재 등록 토큰 가져오기
    private fun checkToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {task ->
            if (!task.isSuccessful) {
                NLog.w("checkToken.. registration token failed.. exception:${task.exception?.stackTraceToString()}")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = "토큰:$token"
            NLog.d("checkToken.. token:$msg")
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        })
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {

        } else {

        }
    }

    private fun askNotificationPermission() {
        // TIRAMISU = API33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // check POST_NOTIFICATIONS permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // 이용자에게 왜 사용해야 하는지 설명
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}