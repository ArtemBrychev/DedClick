package com.example.dedclick.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dedclick.data.AuthManager
import com.example.dedclick.data.model.ContactDto
import com.example.dedclick.data.model.ElderAdapter
import com.example.dedclick.data.model.HeartbeatDto
import com.example.dedclick.databinding.ActivityTrustedHomeBinding
import com.example.dedclick.service.ApiResult
import com.example.dedclick.service.ContactApiProvider
import com.example.dedclick.service.DateTimeUtils
import com.example.dedclick.service.HeartbeatApiProvider
import kotlinx.coroutines.launch

class TrustedHomeActivity  : ComponentActivity(){
    private lateinit var binding: ActivityTrustedHomeBinding
    private lateinit var authManager: AuthManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)

        binding = ActivityTrustedHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authManager = AuthManager(applicationContext)


        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                authManager.clearAuthInfo()
                startActivity(Intent(this@TrustedHomeActivity, MainActivity::class.java))
                finish()
            }
        }

        binding.eldersButton.setOnClickListener {
            startActivity(Intent(this@TrustedHomeActivity, ElderListActivity::class.java))
            finish()
        }


        lifecycleScope.launch {
            val token = authManager.getUserAuthInfo()?.token
            if(token == null){
                Toast.makeText(this@TrustedHomeActivity, "Ошибка при получении данных пользвателя",
                    Toast.LENGTH_LONG).show()
                Log.i("AUTH:MANAGER", "Не удалось получить токен пользователя")
                return@launch
            }

            var apiResult = ContactApiProvider.getContacts(token)
            val message = when(apiResult){
                is ApiResult.Success -> {
                    if(apiResult.data!=null){
                        val contactsList = apiResult.data.filter { it.status!=0 }
                        if (contactsList.isEmpty()) {
                            "Нет активных контактов"
                        }else {
                            /*var heartBeatList = mutableListOf<HeartbeatDto>()
                            for (contact in contactsList) {
                                var heartbeatResult =
                                    HeartbeatApiProvider.getHeartbeat(contact.member.id, token)
                                if (heartbeatResult is ApiResult.Success && heartbeatResult.data != null) {
                                    heartBeatList.add(heartbeatResult.data)
                                }
                            }*/
                            val heartBeatList = contactsList.mapNotNull { contact ->
                                val result = HeartbeatApiProvider.getHeartbeat(contact.member.id, token)
                                if (result is ApiResult.Success) result.data else null
                            }

                            val latest = heartBeatList.maxByOrNull {
                                DateTimeUtils.parseToInstant(it.tappedAt)
                            }

                            if (latest != null) {
                                "От ${latest.user.fullName} • ${
                                    DateTimeUtils.formatSignalTime(
                                        latest.tappedAt
                                    )
                                }"
                            } else {
                                "Пока нет сигналов"
                            }
                        }
                    }else{
                        "Список контактов пуст"
                    }
                }
                is ApiResult.Error -> {
                    when(apiResult.code){
                        401 -> "Невалидные данные пользователя"
                        else -> "Непредвиденная ошибка"
                    }
                }
            }

            binding.lastCallText.text = message
        }
    }

}