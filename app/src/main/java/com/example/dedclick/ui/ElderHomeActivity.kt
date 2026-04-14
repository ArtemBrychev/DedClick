package com.example.dedclick.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.data.AuthManager
import com.example.dedclick.data.model.ContactDto
import com.example.dedclick.databinding.ActivityElderHomeBinding
import com.example.dedclick.databinding.DialogContactRequestBinding
import com.example.dedclick.service.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ElderHomeActivity  : ComponentActivity(){
    private lateinit var binding: ActivityElderHomeBinding
    private lateinit var authManager: AuthManager
    private lateinit var locationProvider: LocationProvider

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation { lat, lon ->
                    Log.i("ELDERHOME:GPS:LOCATION", "From permission result {$lat, $lon}")
                }
            } else {
                Log.i("ELDERHOME:GPS:LOCATION", "Permission denied")
            }
        }

    private val shownRequests = mutableSetOf<Long>()

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)

        binding = ActivityElderHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(applicationContext)
        locationProvider = LocationProvider(applicationContext)

        lifecycleScope.launch {
            val user = authManager.getUserAuthInfo()
            binding.titleTop.text = "Здравствуй, ${user?.name ?: "пользователь"}!"
            if(user == null){
                return@launch
            }
            while (isActive) {
                val result = ContactApiProvider.getContactRequest(user.token)
                if (result is ApiResult.Success && result.data != null) {
                    for(contact in result.data){
                        if (!shownRequests.contains(contact.id)) {
                            shownRequests.add(contact.id)
                            showContactDialog(contact)
                        }
                    }
                }
                delay(30000)
            }
        }

        lifecycleScope.launch {
            val token = authManager.getUserAuthInfo()?.token
            if(token == null){
                Toast.makeText(this@ElderHomeActivity,
                    "Ошибка получения информации пользователя.\n" +
                            "Пожалуйста перезайдите в аккаунт.", Toast.LENGTH_LONG).show()
                return@launch
            }
            val result = UserApiProvider.updateActivity(token)
            when(result){
                is ApiResult.Success -> {
                    Log.i("NETWORK:USER:ACTIVITY", "Информация о активности пользователя передана в ФСБ")
                }
                is ApiResult.Error -> {
                    val message = when(result.code){
                        401 -> "Ошибка получения информации пользователя.\n" +
                                "Пожалуйста перезайдите в аккаунт."
                        else -> null
                    }

                    if(message!=null){
                        Toast.makeText(this@ElderHomeActivity,
                            message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.signalButton.setOnClickListener {
            Log.i("ELDERHOME:GPS:LOCATION", "Before permission check")
            checkPermissionAndGetLocation{lat, lon ->
                lifecycleScope.launch {
                    val token = authManager.getUserAuthInfo()?.token
                    if(token == null){
                        Toast.makeText(this@ElderHomeActivity,
                            "Ошибка получения информации пользователя.\n" +
                                    "Пожалуйста перезайдите в аккаунт.", Toast.LENGTH_LONG).show()
                        return@launch
                    }

                    val result = HeartbeatApiProvider.sendHeartbeat(token, lat, lon)
                    val message = when(result){
                        is ApiResult.Success -> {
                            "Сигнал успешно отправлен"
                            showLastSignal(token)
                        }

                        is ApiResult.Error -> {
                            when(result.code){
                                401 -> "Неправильные данные авторизации, пожалуйста презайдите в аккаунт"
                                else -> "непредвиденная ошибка, попробуйте позже"
                            }
                        }
                    }

                }
            }
            Log.i("ELDERHOME:GPS:LOCATION", "After permission check")
        }


        binding.trustedListButton.setOnClickListener {
            val intent = Intent(this, TrustedContactsActivity::class.java)
            startActivity(intent)
            finish()
        }

        lifecycleScope.launch {
            val token = authManager.getUserAuthInfo()?.token
            if(token == null){
                Toast.makeText(this@ElderHomeActivity,
                    "Ошибка получения информации пользователя.\n" +
                            "Пожалуйста перезайдите в аккаунт.", Toast.LENGTH_LONG).show()
                return@launch
            }

            showLastSignal(token)
        }


        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                authManager.clearAuthInfo()
                startActivity(Intent(this@ElderHomeActivity, MainActivity::class.java))
            }
        }
    }

    fun showContactDialog(contact: ContactDto) {
        val binding = DialogContactRequestBinding.inflate(layoutInflater)

        binding.requestText.text =
            "Контакт ${contact.keeper.fullName} хочет стать вашим доверенным лицом"

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.acceptButton.setOnClickListener {
            lifecycleScope.launch {
                val token = authManager.getUserAuthInfo()?.token
                if(token==null){
                    Toast.makeText(this@ElderHomeActivity,
                        "Ошибка при получении данных пользователя. Перезайдите в приложение",
                        Toast.LENGTH_LONG).show()
                    return@launch
                }
                val result = ContactApiProvider.respondContact(contact.id, token)
                when(result){
                    is ApiResult.Success -> {
                        //Тут надо  вывести другое окно о принятии
                        Toast.makeText(this@ElderHomeActivity,
                            "Контакт добавлен",
                            Toast.LENGTH_LONG).show()
                    }
                    is ApiResult.Error -> {
                        val message = when(result.code){
                            401 -> "Ошибка при получении данных пользователя. Перезайдите в приложение"
                            else -> "Непрдевиденная ошибка. Попробуйте позже"
                        }
                        Toast.makeText(this@ElderHomeActivity, message, Toast.LENGTH_LONG).show()
                    }
                }

                dialog.dismiss()
            }
        }

        binding.rejectButton.setOnClickListener {
            lifecycleScope.launch {
                val token = authManager.getUserAuthInfo()?.token
                if(token==null){
                    Toast.makeText(this@ElderHomeActivity,
                        "Ошибка при получении данных пользователя. Перезайдите в приложение",
                        Toast.LENGTH_LONG).show()
                    return@launch
                }
                val result = ContactApiProvider.deleteContact(contact.id, token)
                val message = when(result){
                    is ApiResult.Success -> {
                        "Запрос не принят"
                    }
                    is ApiResult.Error -> {
                        when(result.code){
                            401 -> "Ошибка при получении данных пользователя. Перезайдите в приложение"
                            else -> "Непрдевиденная ошибка. Попробуйте позже"
                        }
                    }
                }

                Toast.makeText(this@ElderHomeActivity, message, Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun checkPermissionAndGetLocation(onResult: (Double?, Double?) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation(onResult)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getLocation(onResult: (Double?, Double?) -> Unit) {
        locationProvider.getCurrentLocation { location ->
            val lat: Double?
            val lon: Double?

            if (location != null) {
                lat = location.latitude
                lon = location.longitude
            } else {
                lat = null
                lon = null
            }

            Log.i("ELDERHOME:GPS:LOCATION", "Current location is{$lat, $lon}")

            onResult(lat, lon)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun showLastSignal(token: String){
        val result = HeartbeatApiProvider.getSelfHeartbeat(token)
        when(result){
            is ApiResult.Success -> {
                val userHeartbeat = result.data
                if(result.data!=null){
                    Log.i("NETWORK:LASTSIGNAL:TEXT", "Raw ${userHeartbeat.tappedAt}")
                    val date = DateTimeUtils.formatSignalTime(userHeartbeat.tappedAt)
                    binding.lastSignalText.text = "Последний сигнал был отправлен\n$date"
                    Log.i("NETWORK:LASTSIGNAL:TEXT", "Parsed $date")
                }
            }
            is ApiResult.Error->{
                val message = when(result.code){
                    400, 401, 403 -> "Доступ к данной информации запрещен"
                    else -> "Что то пошло не так"
                }
                Log.i("NETWORK:LASTSIGNAL:TEXT", message)
            }
        }
    }

}