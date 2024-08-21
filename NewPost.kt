package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.data.Post
import com.example.myapplication.data.FirestoreService
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewPost : AppCompatActivity() {

    private val firestoreService = FirestoreService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_post)

        val titleEditText = findViewById<EditText>(R.id.editTextTitle)
        val contentEditText = findViewById<EditText>(R.id.editTextContent)
        val submitButton = findViewById<Button>(R.id.buttonSubmit)

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val currentDate = Timestamp.now()
                val newPost = Post(title = title, content = content, date = currentDate)

                CoroutineScope(Dispatchers.IO).launch {
                    val postId = firestoreService.insertPost(newPost)
                    if (postId != null) {
                        // Post ID를 추가된 게시물의 ID로 업데이트
                        firestoreService.updatePost(postId, mapOf("id" to postId))
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@NewPost, "글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            finish()  // 글 작성 후 액티비티 종료
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@NewPost, "글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
