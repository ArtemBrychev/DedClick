package com.example.dedclick.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dedclick.data.AuthManager
import com.example.dedclick.data.model.ContactDto
import com.example.dedclick.data.model.ContactsAdapter
import com.example.dedclick.data.model.ElderAdapter
import com.example.dedclick.databinding.ActivityElderHomeBinding
import com.example.dedclick.databinding.ActivityElderListBinding
import com.example.dedclick.databinding.ActivityTrustedHomeBinding
import com.example.dedclick.service.ApiResult
import com.example.dedclick.service.ContactApiProvider
import kotlinx.coroutines.launch

class ElderListActivity : ComponentActivity(){
    private lateinit var binding: ActivityElderListBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)
        binding = ActivityElderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authManager = AuthManager(applicationContext)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, TrustedHomeActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val token = authManager.getUserAuthInfo()?.token
            if(token == null){
                Toast.makeText(this@ElderListActivity, "Ошибка при получении данных пользвателя",
                    Toast.LENGTH_LONG).show()
                Log.i("AUTH:MANAGER", "Не удалось получить токен пользователя")
                return@launch
            }

            binding.addButton.setOnClickListener {
                val phone = binding.phoneInputField.text.toString()
                if(!isValidPhone(phone)){
                    Toast.makeText(this@ElderListActivity, "Введите корректный номер телефона",
                        Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                lifecycleScope.launch {
                    val result = ContactApiProvider.addContact(token, phone)
                    val message = when(result){
                        is ApiResult.Success -> {
                            "Запрос успешно отправлен пользователю: $phone"
                        }
                        is ApiResult.Error -> {
                            when(result.code){
                                400 -> "Невозможно добавить контакт\n" +
                                        "Пользователь $phone является доверенным лицом"
                                401 -> "Ошибка при аутентификации, перезайдите в аккаунт"
                                404 -> "Такого пользователя не существует"
                                else -> "Непредвиденная ошибка, попробуйте позже"
                            }
                        }
                    }
                    Toast.makeText(this@ElderListActivity,
                        message,
                        Toast.LENGTH_LONG).show()
                }
            }



            var apiResult = ContactApiProvider.getContacts(token)
            when(apiResult){
                is ApiResult.Success -> {
                    val contactsList = if(apiResult.data!=null){
                        apiResult.data.filter { it.status!=0 }
                    }else{
                        Toast.makeText(this@ElderListActivity, "Список контактов пуст",
                            Toast.LENGTH_LONG).show()
                        emptyList<ContactDto>()
                    }

                    val adapter = ElderAdapter(
                        contactsList,
                        onDeleteClick = { contact ->
                            lifecycleScope.launch {
                                val deleteResult = ContactApiProvider.deleteContact(contact.id, token)

                                val message = when(deleteResult){
                                    is ApiResult.Success -> "Контакт успешно удален"
                                    is ApiResult.Error -> when(deleteResult.code){
                                        401, 403 -> "Данное действие не доступно"
                                        404 -> "Такого контакта не существует"
                                        else -> "Непредвиденная ошибка"
                                    }
                                }

                                Toast.makeText(this@ElderListActivity, message, Toast.LENGTH_LONG).show()
                                recreate()
                            }
                        },

                        onInfoClick = { contact ->
                            val user = contact.member
                            Log.d("INFO:ID", user.id.toString())
                            val intent = Intent(this@ElderListActivity, InfoScreen::class.java)
                            intent.putExtra("id", user.id)
                            startActivity(intent)
                            finish()
                        }
                    )

                    binding.elderRecyclerView.layoutManager =
                        LinearLayoutManager(this@ElderListActivity)

                    binding.elderRecyclerView.adapter = adapter
                }
                is ApiResult.Error -> {
                    val message = when(apiResult.code){
                        401 -> "Невалидные данные пользователя"
                        else -> "Непредвиденная ошибка"
                    }
                    Toast.makeText(this@ElderListActivity, message,
                        Toast.LENGTH_LONG).show()
                    return@launch
                }
            }

        }

    }

    fun isValidPhone(phone: String): Boolean {
        val regex = Regex("^\\+?[0-9]\\d{10,14}$")
        return regex.matches(phone)
    }
}