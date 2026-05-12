package com.example.dedclick.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.dedclick.R
import com.example.dedclick.data.AuthManager
import com.example.dedclick.service.FirebaseApiProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM", "NEW TOKEN: $token")

        CoroutineScope(Dispatchers.IO).launch {
            sendTokenToBackend(token)
        }
    }

    suspend fun sendTokenToBackend(token: String) : ApiResult<Unit>{
        val authManager = AuthManager(applicationContext)// Как можно принимать контекст приложения? Для дата стора по идее все должно работать

        val authInfo = authManager.getUserAuthInfo();
        val phone = if(authInfo!=null) authInfo.phone else null
        val userToken = if(authInfo!=null) authInfo.token else null

        if (phone!=null && userToken!=null) {
            val apiResult = FirebaseApiProvider.sendFirebaseToken(userToken, phone, token)
            return apiResult
        }else{
            return ApiResult.Error(-1, "Не удалось получить данные пользователя")
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "No title"
        val body = remoteMessage.notification?.body ?: "No body"

        Log.d("FCM", "Message: $title $body")

        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "default_channel"

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        manager.notify(1, notification)
    }
}