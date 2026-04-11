package com.example.dedclick.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.R
import com.example.dedclick.databinding.ActivityRegisterBinding
import com.example.dedclick.service.ApiResult
import com.example.dedclick.service.AuthApiProvider
import kotlinx.coroutines.launch

class RegisterActivity: ComponentActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val priveousButton = binding.priveousButton
        priveousButton.setOnClickListener {
            val intentNew = Intent(this, MainActivity::class.java)
            startActivity(intentNew)
        }

        val nameInput = binding.inputName
        val phoneInput = binding.inputPhone

        var isElder = true
        val elderRoleButton = binding.roleElder
        val trustedRoleButton = binding.roleTrusted

        elderRoleButton.setOnClickListener {
            if(!isElder){
                elderRoleButton.setBackgroundResource(R.drawable.bg_role_selected)
                trustedRoleButton.setBackgroundResource(R.drawable.bg_role_unselected)
                isElder = true
            }
        }

        trustedRoleButton.setOnClickListener {
            if(isElder){
                elderRoleButton.setBackgroundResource(R.drawable.bg_role_unselected)
                trustedRoleButton.setBackgroundResource(R.drawable.bg_role_selected)
                isElder = false
            }
        }

        var registerButton = binding.btnRegister
        registerButton.setOnClickListener {
            val username = nameInput.text.toString()
            val phone = phoneInput.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(this, "Пожалуйста введите ваше имя", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                Toast.makeText(this, "Пожалуйста введите ваш номер телефона", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!isValidPhone(phone)) {
                Toast.makeText(this, "Некорректный формат номера", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(this, CodeActivity::class.java)
            intent.putExtra("previous", RegisterActivity::class.java)
            intent.putExtra("phone", phone)

            lifecycleScope.launch {
                val role = if (isElder) "elder" else "trusted"
                val result = AuthApiProvider.register(phone, username, role)
                when (result) {
                    is ApiResult.Success -> {
                        Log.i("NETWORK:AUTH:REGISTER", "Запрос был успешен")
                        startActivity(intent)
                        finish()
                    }
                    is ApiResult.Error -> {
                        Log.i(
                            "NETWORK:AUTH:REGISTER",
                            "Запрос не был успешен.\nCode: ${result.code} + Message: ${result.message}"
                        )
                        val message = when(result.code){
                            400 -> "Неккоректный номер телефона"
                            404 -> "Неккоректная роль"
                            409 -> "Данный номер телефона уже занят"
                            422 -> "Номер телефона не отправлен"
                            else -> "Ошибка сети, попробуйте позже"
                        }
                        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    fun isValidPhone(phone: String): Boolean {
        val regex = Regex("^\\+?[0-9]\\d{10,14}$")
        return regex.matches(phone)
    }
}