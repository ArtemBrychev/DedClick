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
import com.example.dedclick.databinding.ActivityElderHomeBinding
import com.example.dedclick.databinding.ActivityTrustedContactsBinding
import com.example.dedclick.service.ApiResult
import com.example.dedclick.service.ContactApiProvider
import kotlinx.coroutines.launch

class TrustedContactsActivity : ComponentActivity(){
    private lateinit var binding: ActivityTrustedContactsBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)
        binding = ActivityTrustedContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authManager = AuthManager(applicationContext)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, ElderHomeActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val token = authManager.getUserAuthInfo()?.token
            if(token == null){
                Toast.makeText(this@TrustedContactsActivity, "Ошибка при получении данных пользвателя",
                    Toast.LENGTH_LONG).show()
                Log.i("AUTH:MANAGER", "Не удалось получить токен пользователя")
                return@launch
            }
            var apiResult = ContactApiProvider.getContacts(token)
            when(apiResult){
                is ApiResult.Success -> {
                    val contactsList = if(apiResult.data!=null){
                        apiResult.data.filter { it.status!=0 }
                    }else{
                        Toast.makeText(this@TrustedContactsActivity, "Список контактов пуст",
                            Toast.LENGTH_LONG).show()
                        emptyList<ContactDto>()
                    }

                    val adapter = ContactsAdapter(contactsList) { contact ->
                        lifecycleScope.launch {
                            val deleteResult = ContactApiProvider.deleteContact(contact.id, token)
                            lateinit var message:String
                            when(deleteResult){
                                is ApiResult.Success -> {
                                    message = "Контакт успешно удален"
                                }
                                is ApiResult.Error -> {
                                    message = when(deleteResult.code){
                                        401, 403 -> "Данное действие не доступно"
                                        404 -> "Такого контакта не существуе"
                                        else -> "Непредвиденная ошибка"
                                    }
                                }
                            }
                            Toast.makeText(this@TrustedContactsActivity, message,
                                Toast.LENGTH_LONG).show()
                            recreate()
                        }
                    }

                    binding.trustedRecyclerView.layoutManager =
                        LinearLayoutManager(this@TrustedContactsActivity)

                    binding.trustedRecyclerView.adapter = adapter
                }
                is ApiResult.Error -> {
                    val message = when(apiResult.code){
                        401 -> "Невалидные данные пользователя"
                        else -> "Непредвиденная ошибка"
                    }
                    Toast.makeText(this@TrustedContactsActivity, message,
                        Toast.LENGTH_LONG).show()
                    return@launch
                }
            }

        }

    }
}