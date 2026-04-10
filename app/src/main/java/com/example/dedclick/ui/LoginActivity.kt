package com.example.dedclick.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.data.AuthManager
import com.example.dedclick.databinding.ActivityLoginBinding
import com.example.dedclick.service.ApiResult
import com.example.dedclick.service.AuthApiProvider
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(applicationContext)

        //Кнопка назад
        binding.priveousButton.setOnClickListener {
            val intentNew = Intent(this, MainActivity::class.java)
            startActivity(intentNew)
        }

        binding.loginButton.setOnClickListener {
            val phone = binding.phoneInput.text.toString()
            if(!isValidPhone(phone)){
                Toast.makeText(this, "Неккоректный номер телефона", Toast.LENGTH_LONG)
                return@setOnClickListener
            }

            lifecycleScope.launch {
                var result = AuthApiProvider.generateCode(phone)

                when(result){
                    is ApiResult.Success<*> -> {
                        Log.i("NETWORK:AUTH:GENERATECODE", "Запрос на генерацию кода успешно прошел")

                        val intent = Intent(this@LoginActivity, CodeActivity::class.java)
                        intent.putExtra("phone", phone)
                        intent.putExtra("previous", LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is ApiResult.Error -> {
                        when(result.code){
                            401 -> {
                                Log.e("NETWORK:AUTH:GENERATECODE", "Запрос на генерацию кода был не успешным")
                                Toast.makeText(this@LoginActivity, "Такого пользователя не существует", Toast.LENGTH_LONG)
                                return@launch
                            }
                            else -> {
                                Log.e("NETWORK:AUTH:GENERATECODE", "Запрос на генерацию кода был не успешным")
                                Toast.makeText(this@LoginActivity, "Ошибка, попробуйте позже", Toast.LENGTH_LONG)
                                return@launch
                            }
                        }

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