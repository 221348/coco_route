package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.content.Intent
//import com.example.faq.network.ApiService
//import com.example.faq.network.LoginRequest
//import com.example.faq.network.LoginResponse
import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.synthetic.main.activity_login.*
import android.view.View
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var idEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView
    private lateinit var forgotPasswordTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Initialize views
        idEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signUpTextView = findViewById(R.id.signUpTextView)
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)
        auth = FirebaseAuth.getInstance()

        // Set up listeners
        loginButton.setOnClickListener {
            handleLogin()
        }

        signUpTextView.setOnClickListener {
            // Handle sign up
            // Start SignUpActivity when the user clicks on Sign Up
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        forgotPasswordTextView.setOnClickListener {
            // Handle forgot password
            // Start ForgotPasswordActivity when the user clicks on Forgot Password
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin() {
        val id = idEditText.text.toString()
        val password = passwordEditText.text.toString()
        val rootView = findViewById<View>(android.R.id.content)

        // Validate input and perform login action
        if (id.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "ID와 비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            return
        } else if (id.isEmpty()) {
            Toast.makeText(this, "ID를 입력해주세요!", Toast.LENGTH_SHORT).show()
            return
        } else if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            return
        }

        // Perform login logic with Firebase
        auth.signInWithEmailAndPassword(id, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to another activity
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Close the current activity
                } else {
                    // Login failed, show error message
                    Snackbar.make(
                        rootView,
                        "로그인에 실패하였습니다: ${task.exception?.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }
}