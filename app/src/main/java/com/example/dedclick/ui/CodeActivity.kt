package com.example.dedclick.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.widget.addTextChangedListener
import com.example.dedclick.databinding.ActivityCodeBinding

class CodeActivity : ComponentActivity() {

    private lateinit var binding: ActivityCodeBinding
    private lateinit var hiddenInput: EditText
    private lateinit var boxes: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
