package com.example.dedclick.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.data.AuthManager
import com.example.dedclick.data.management.RoleDataStoreManager
import com.example.dedclick.data.management.TokenDataStoreManager
import com.example.dedclick.data.model.UserAuthInfo
import com.example.dedclick.databinding.ActivityCodeBinding
import com.example.dedclick.service.ApiResult
import com.example.dedclick.service.AuthApiProvider
import kotlinx.coroutines.launch
import kotlin.math.log

class CodeActivity : ComponentActivity() {

    //TODO: Переписать сохранение авторизации пользователя на получение запроса с сервера

    private lateinit var binding: ActivityCodeBinding
    private lateinit var hiddenInput: EditText
    private lateinit var boxes: List<TextView>

    private lateinit var authManager: AuthManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(applicationContext)

        // Кнопка назад
        binding.priveousButton.setOnClickListener {
            val clazz = intent.getSerializableExtra("previous") as Class<*>
            startActivity(Intent(this, clazz))
        }

        // Создаём TextView внутри каждого квадрата
        boxes = listOf(
            createOverlayText(binding.box1),
            createOverlayText(binding.box2),
            createOverlayText(binding.box3),
            createOverlayText(binding.box4)
        )

        hiddenInput = binding.hiddenCodeInput

        // Клик по квадратикам — фокус на EditText
        binding.codeBoxesContainer.setOnClickListener {
            hiddenInput.requestFocus()
            showKeyboard(hiddenInput)
        }

        // Обновляем квадраты при вводе
        hiddenInput.addTextChangedListener {
            updateBoxes(it.toString())
        }

        val acceptButton = binding.acceptButton

        /////////////////////////////////////
        // ОТПРАВКА ДАННЫХ ДЛЯ АВТОРИЗАЦИИ //
        /////////////////////////////////////
        acceptButton.setOnClickListener {
            lifecycleScope.launch {
                val code = hiddenInput.text.toString()
                val username = intent.getStringExtra("username")
                val phone = intent.getStringExtra("phone")
                val isElder = intent.getBooleanExtra("isElder", true)
                val role = if (isElder) "elder" else "trusted"

                if (code.length != 4 || username == null || phone == null) {
                    Toast.makeText(this@CodeActivity, "Код не корректный", Toast.LENGTH_LONG).show()
                    return@launch
                }

                val result = AuthApiProvider.login(phone, code)

                when (result) {
                    is ApiResult.Success<String> -> {
                        val token = result.data

                        if (token == null) {
                            Log.i(
                                "NETWORK:AUTH:LOGIN",
                                "Не удалось получить токен"
                            )
                            Toast.makeText(
                                this@CodeActivity,
                                "Ошибка. Попробуйте позже",
                                Toast.LENGTH_LONG
                            ).show()
                            return@launch
                        }

                        val userInfo = UserAuthInfo(token, username, phone, role)
                        authManager.saveUserAuthInfo(userInfo)

                        val nextIntent = if (role == "trusted") {
                            Intent(this@CodeActivity, TrustedHomeActivity::class.java)
                        } else {
                            Intent(this@CodeActivity, ElderHomeActivity::class.java)
                        }

                        startActivity(nextIntent)
                        finish()
                    }

                    is ApiResult.Error -> {
                        Log.i(
                            "NETWORK:AUTH:LOGIN",
                            "Запрос не был успешен.\nCode: ${result.code} + Message: ${result.message}"
                        )

                        Toast.makeText(this@CodeActivity, "Неправильный код, либо пользователя не существует", Toast.LENGTH_LONG).show()
                        return@launch
                    }
                }
            }
        }
    }

    private fun updateBoxes(code: String) {
        boxes.forEachIndexed { index, tv ->
            tv.text = if (index < code.length) code[index].toString() else ""
        }
    }

    private fun createOverlayText(box: FrameLayout): TextView {
        val tv = TextView(this)
        tv.textSize = 24f
        tv.setTextColor(Color.BLACK)
        tv.gravity = Gravity.CENTER

        box.addView(tv, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))

        return tv
    }

    private fun showKeyboard(view: View) {
        view.post {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }


}
