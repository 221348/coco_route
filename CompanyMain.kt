package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CompanyMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_main)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val button1 = findViewById<Button>(R.id.button1) // 기업 A (삼성전자)
        val button2 = findViewById<Button>(R.id.button2) // 기업 B (SK)
        val button3 = findViewById<Button>(R.id.button3) // 기업 C (현대자동차)
        val button4 = findViewById<Button>(R.id.button4) // 기업 D (LG)
        val button5 = findViewById<Button>(R.id.button5) // 기업 E (롯데)
        val button6 = findViewById<Button>(R.id.button6) // 기업 F (포스코)
        val button7 = findViewById<Button>(R.id.button7) // 기업 G (한화)
        val button8 = findViewById<Button>(R.id.button8) // 기업 H (현대중공업)

        // 버튼 클릭 리스너
        val buttonClickListener = { companyName: String ->
            val intent = Intent(this, CompanyDetail::class.java)
            intent.putExtra("companyName", companyName)
            startActivity(intent)
        }

        button1.setOnClickListener { buttonClickListener("삼성전자") }
        button2.setOnClickListener { buttonClickListener("SK") }
        button3.setOnClickListener { buttonClickListener("현대자동차") }
        button4.setOnClickListener { buttonClickListener("LG") }
        button5.setOnClickListener { buttonClickListener("롯데") }
        button6.setOnClickListener { buttonClickListener("포스코") }
        button7.setOnClickListener { buttonClickListener("한화") }
        button8.setOnClickListener { buttonClickListener("현대중공업") }

        // 검색 기능
        findViewById<Button>(R.id.searchButton).setOnClickListener {
            val searchText = searchEditText.text.toString().trim().lowercase()
            when (searchText) {
                "삼성", "삼성전자", "samsung" -> buttonClickListener("삼성전자")
                "sk" , "SK", "Sk" -> buttonClickListener("SK")
                "현대자동차" -> buttonClickListener("현대자동차")
                "lg" , "LG", "Lg"-> buttonClickListener("LG")
                "롯데" -> buttonClickListener("롯데")
                "포스코" -> buttonClickListener("포스코")
                "한화" -> buttonClickListener("한화")
                "현대중공업" -> buttonClickListener("현대중공업")
                // 다른 기업 이름 추가
            }
        }
    }
}