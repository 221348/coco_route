package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FAQMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.faq_main)

        // 질문과 답변 뷰를 연결
        val question1: TextView = findViewById(R.id.question1)
        val answer1: TextView = findViewById(R.id.answer1)
        val question2: TextView = findViewById(R.id.question2)
        val answer2: TextView = findViewById(R.id.answer2)
        val question3: TextView = findViewById(R.id.question3)
        val answer3: TextView = findViewById(R.id.answer3)


        // 질문 1 클릭 리스너
        question1.setOnClickListener {
            toggleVisibility(answer1, question1)
        }

        // 질문 2 클릭 리스너
        question2.setOnClickListener {
            toggleVisibility(answer2, question2)
        }

        // 질문 3 클릭 리스너
        question3.setOnClickListener {
            toggleVisibility(answer3, question3)
        }
    }

    // 답변 뷰의 가시성을 토글하는 함수
    private fun toggleVisibility(answerView: View, questionView: TextView) {
        if (answerView.visibility == View.GONE) {
            answerView.visibility = View.VISIBLE
            // 아이콘 변경: ic_arrow_down으로
            questionView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0)
        } else {
            answerView.visibility = View.GONE
            // 아이콘 변경: ic_arrow_up으로
            questionView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_up, 0, 0, 0)
        }
    }
}