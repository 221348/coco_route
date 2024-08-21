package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.FirestoreService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPost : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var buttonSave: Button
    private val firestoreService = FirestoreService()
    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_post)

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)
        buttonSave = findViewById(R.id.buttonSave)

        postId = intent.getStringExtra("postId")

        postId?.let { id ->
            loadPostDetails(id)
        }

        buttonSave.setOnClickListener {
            savePost()
        }
    }

    private fun loadPostDetails(postId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val post = firestoreService.getPostById(postId)
            withContext(Dispatchers.Main) {
                post?.let {
                    editTextTitle.setText(it.title)
                    editTextContent.setText(it.content)
                }
            }
        }
    }

    private fun savePost() {
        val title = editTextTitle.text.toString()
        val content = editTextContent.text.toString()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        postId?.let { id ->
            val updatedPost = mapOf(
                "title" to title,
                "content" to content
            )

            CoroutineScope(Dispatchers.IO).launch {
                val success = firestoreService.updatePost(id, updatedPost)
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(this@EditPost, "게시물이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()  // 수정 후 액티비티 종료
                    } else {
                        Toast.makeText(this@EditPost, "게시물 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
