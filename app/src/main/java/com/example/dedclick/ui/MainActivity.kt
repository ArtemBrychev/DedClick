package com.example.dedclick.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.data.AuthManager
import com.example.dedclick.data.management.RoleDataStoreManager
import com.example.dedclick.data.management.TokenDataStoreManager
import com.example.dedclick.databinding.ActivityStartBinding
import com.example.dedclick.service.ApiResult
import com.example.dedclick.service.FirebaseApiProvider
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityStartBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        authManager = AuthManager(applicationContext)
        lifecycleScope.launch {

            val userInfo = authManager.getUserAuthInfo() ?: return@launch


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    requestPermissions(
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        1001
                    )
                }
            }

            val phone = userInfo.phone
            val usertoken = userInfo.token
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { firebaseToken ->
                    sendToken(usertoken, phone, firebaseToken)
                }

            val intent = when (userInfo.role) {
                "elder" -> Intent(this@MainActivity, ElderHomeActivity::class.java)
                "trusted" -> Intent(this@MainActivity, TrustedHomeActivity::class.java)
                else -> return@launch
            }

            startActivity(intent)
            finish()
        }
        setContentView(binding.root)

        val loginButton = binding.loginButton
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("previous", MainActivity::class.java)
            startActivity(intent)
        }

        val registerButton = binding.registerButton
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("previous", MainActivity::class.java)
            startActivity(intent)
        }
    }

     fun sendToken(usertoken:String, phone:String, firebasetoken:String){
        CoroutineScope(Dispatchers.IO).launch {
            val apiResult = FirebaseApiProvider.sendFirebaseToken(usertoken, phone, firebasetoken)
            Log.d("FCM:MAIN:ACTIVITY", firebasetoken)
            if(apiResult is ApiResult.Error){
                Log.d("FCM:MAIN:ACTIVITY",
                    "Не удалось отправить firebase токен (${apiResult.code}:${apiResult.message})")
            }
        }
    }
}