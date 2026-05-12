package com.example.dedclick.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.R
import com.example.dedclick.data.AuthManager
import com.example.dedclick.databinding.ActivityInfoBinding
import com.example.dedclick.service.ApiResult
import com.example.dedclick.service.DateTimeUtils
import com.example.dedclick.service.HeartbeatApiProvider
import com.example.dedclick.service.UserApiProvider
import kotlinx.coroutines.launch

class InfoScreen : ComponentActivity() {
    private lateinit var binding: ActivityInfoBinding
    private lateinit var authManager: AuthManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceStore: Bundle?) {
        super.onCreate(savedInstanceStore)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authManager = AuthManager(applicationContext)

        binding.priveousButton.setOnClickListener {
            startActivity(Intent(this, ElderListActivity::class.java))
        }

        lifecycleScope.launch {
            Log.d("INFO:ID:INFOSCREEN", "${intent.getStringExtra("id")}")
            val id = intent.getLongExtra("id", -1)
            val token = authManager.getUserAuthInfo()?.token
            if(id==-1L){
                Toast.makeText(this@InfoScreen, "Ну пиздец. И где id", Toast.LENGTH_LONG).show()
                return@launch
            }
            if(token==null){
                Toast.makeText(this@InfoScreen, "Ну пиздец. И где token", Toast.LENGTH_LONG).show()
                return@launch
            }

            val result = HeartbeatApiProvider.getHeartbeat(id, token)

            when(result){
                is ApiResult.Success -> {
                    val userInfo = result.data
                    if(userInfo == null){
                        return@launch
                    }
                    val info = userInfo.user
                    if(info == null) {
                        return@launch
                    }
                    binding.contactName.text = if(info.fullName!=null){
                        info.fullName
                    }else{
                        "Не удалось получить имя"
                    }
                    binding.lastSignalText.text = if(userInfo.tappedAt.isNotEmpty()){
                        "Последний check-in:" +
                            DateTimeUtils.formatSignalTime(userInfo.tappedAt)
                    }else{
                        "Не удалось получить время"
                    }
                    binding.coords.text = if(userInfo.lat != null && userInfo.lon!=null){
                        "${userInfo.lat}, ${userInfo.lon}"
                    }else {"Местоположение не известно"}

                    if(userInfo.status == "OK"){
                        binding.statusBadge.text = "В порядке"
                        binding.statusBadge.setBackgroundResource(R.drawable.bg_status_ok)
                    }else if(userInfo.status == "WARN"){
                        binding.statusBadge.text = "Check-in пропущен"
                        binding.statusBadge.setBackgroundResource(R.drawable.bg_status_warn)
                    }else if(userInfo.status == "ALERT"){
                        binding.statusBadge.text = "Check-in пропущен на 3 часа"
                        binding.statusBadge.setBackgroundResource(R.drawable.bg_status_alert)
                    }else{
                        binding.statusBadge.visibility = View.GONE
                    }
                }
                is ApiResult.Error -> {
                    val message = when(result.code){
                        401, 403 -> "Нет доступа"
                        404 -> "Такого пользователя не существует"
                        else -> "Непрдвиденная ошибка"
                    }
                    if(token==null){
                        Toast.makeText(this@InfoScreen, message, Toast.LENGTH_LONG).show()
                        Log.i("NETWORK:HEARTBEAT:INFO", message)
                        return@launch
                    }
                }
            }
        }
    }
}