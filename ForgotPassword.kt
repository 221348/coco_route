package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)

        // FirebaseAuth 인스턴스 초기화
        auth = FirebaseAuth.getInstance()

        // UI 요소 초기화
        emailEditText = findViewById(R.id.idEditText)
        resetPasswordButton = findViewById(R.id.resetPasswordButton)

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            // 이메일과 이름 입력 확인
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase로 비밀번호 재설정 이메일 전송
            sendPasswordResetEmail(email)
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "비밀번호 재설정 이메일을 전송했습니다!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "이메일 전송에 실패했습니다. 이메일 주소를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}