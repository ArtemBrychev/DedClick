package com.example.dedclick

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.dedclick.databinding.ActivityRegisterBinding

class RegisterActivity: ComponentActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val priveousButton = binding.priveousButton
        priveousButton.setOnClickListener {
            val clazz = intent.getSerializableExtra(
                "previous"
            ) as Class<*>

            val intentNew = Intent(this, clazz)
            startActivity(intentNew)
        }
    }
}