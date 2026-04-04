package com.example.dedclick.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.dedclick.R
import com.example.dedclick.databinding.ActivityRegisterBinding

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
            //testDataInput(this, username, phone, isElder)

            if(!username.isEmpty() && !phone.isEmpty()) {
                val intent = Intent(this, CodeActivity::class.java)
                intent.putExtra("previous", RegisterActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("phone", phone)
                intent.putExtra("isElder", isElder)
                startActivity(intent)
            }else if(username.isEmpty()){
                Toast.makeText(this, "Пожалуйста введите ваше имя", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Пожалуйста введите ваш номер телефона", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun testDataInput(context: Context, name:String, phone:String, isElder:Boolean){
        Toast.makeText(context, "name: $name", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "phone: $phone", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "is an elder: $isElder", Toast.LENGTH_SHORT).show()
    }
}