package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View
import com.google.android.material.snackbar.Snackbar

class SignUp : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signUpButton = findViewById(R.id.signUpButton)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        signUpButton.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val rootView = findViewById<View>(android.R.id.content)

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(this, "이메일, 비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show()
            return
        }
        else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "이메일을 입력하세요!", Toast.LENGTH_SHORT).show()
            return
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase를 사용하여 사용자 등록
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공 처리
                    val user = auth.currentUser
                    user?.let {
                        val userId = it.uid

                        // Firestore에 사용자 정보 저장
                        val userMap = hashMapOf(
                            "email" to email
                        )

                        firestore.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "환영합니다!", Toast.LENGTH_SHORT).show()

                                // 로그인 액티비티로 이동
                                val intent = Intent(this@SignUp, Login::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Snackbar.make(rootView, "회원정보 저장에 실패하였습니다: ${e.message}", Snackbar.LENGTH_LONG).show()
                            }
                    }
                } else {
                    // 회원가입 실패 처리
                    Snackbar.make(rootView, "회원가입에 실패하였습니다: ${task.exception?.message}", Snackbar.LENGTH_LONG).show()
                }
            }
    }
}