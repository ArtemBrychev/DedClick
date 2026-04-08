package com.example.dedclick.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.data.RoleDataStoreManager
import com.example.dedclick.data.TokenDataStoreManager
import com.example.dedclick.databinding.ActivityStartBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityStartBinding
    private lateinit var tokenDataStoreManager: TokenDataStoreManager
    private lateinit var roleDataStoreManager: RoleDataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        tokenDataStoreManager = TokenDataStoreManager(applicationContext)
        roleDataStoreManager = RoleDataStoreManager(applicationContext)
        lifecycleScope.launch {
            val token = tokenDataStoreManager.getValue().first() ?: return@launch
            val role = roleDataStoreManager.getValue().first() ?: return@launch

            val intent = when (role) {
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
}