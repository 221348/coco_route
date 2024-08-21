package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var textViewFootprintValue: TextView

    // 각 버튼에 대해 다른 증가 값 설정 (Double 타입 사용)
    private val incrementValues = mapOf(
        R.id.buttonWalk to 0.05,    // 버튼 클릭 시 0.05 증가
        R.id.buttonBicy to 0.16,    // 버튼 클릭 시 0.16 증가
        R.id.buttonCar to 27.0,     // 버튼 클릭 시 27.0 증가
        R.id.buttonBus to 8.0,      // 버튼 클릭 시 8.0 증가
        R.id.buttonSubway to 6.0,   // 버튼 클릭 시 6.0 증가
        R.id.buttonTrain to 3.0     // 버튼 클릭 시 3.0 증가
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // XML 레이아웃 파일을 적절히 변경하세요.

        textViewFootprintValue = findViewById(R.id.textViewFootprintValue)

        // 각 ImageButton에 클릭 리스너 설정
        incrementValues.keys.forEach { buttonId ->
            setupImageButton(buttonId)
        }

        // 버튼 클릭 리스너 설정
        setupNavigationButtons()
    }

    private fun setupImageButton(buttonId: Int) {
        val imageButton: ImageButton = findViewById(buttonId)
        imageButton.setOnClickListener {
            incrementTextView(buttonId)
        }
    }

    private fun incrementTextView(buttonId: Int) {
        val incrementValue = incrementValues[buttonId] ?: 0.0
        val currentValueString = textViewFootprintValue.text.toString()
        val currentValue = currentValueString.toDoubleOrNull() ?: 0.0
        val newValue = currentValue + incrementValue

        // 소수점 두 자리로 포맷팅
        val formattedValue = String.format("%.2f", newValue)
        textViewFootprintValue.text = formattedValue
    }

    private fun setupNavigationButtons() {
        val buttonStore: Button = findViewById(R.id.buttonStore)
        val buttonCompany: Button = findViewById(R.id.buttonCompany)
        val buttonCommunity: Button = findViewById(R.id.buttonCommunity)
        val buttonGuide: Button = findViewById(R.id.buttonGuide)

        buttonStore.setOnClickListener {
            val intent = Intent(this, ProductMain::class.java)
            startActivity(intent)
        }

        buttonCompany.setOnClickListener {
            val intent = Intent(this, CompanyMain::class.java)
            startActivity(intent)
        }

        buttonCommunity.setOnClickListener {
            val intent = Intent(this, CommuMain::class.java)
            startActivity(intent)
        }

        buttonGuide.setOnClickListener {
            val intent = Intent(this, FAQMain::class.java)
            startActivity(intent)
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}
