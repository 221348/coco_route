package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.FirestoreService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditComment : AppCompatActivity() {

    private lateinit var editTextComment: EditText
    private lateinit var buttonUpdateComment: Button
    private val firestoreService = FirestoreService()
    private var commentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_comment)

        editTextComment = findViewById(R.id.editTextComment)
        buttonUpdateComment = findViewById(R.id.buttonUpdateComment)

        // commentId와 commentContent를 Intent에서 가져옴
        commentId = intent.getStringExtra("commentId")
        val commentContent = intent.getStringExtra("commentContent")

        // EditText에 기존 댓글 내용 설정
        editTextComment.setText(commentContent)

        buttonUpdateComment.setOnClickListener {
            updateComment()
        }
    }

    private fun updateComment() {
        val updatedContent = editTextComment.text.toString()

        if (commentId.isNullOrEmpty() || updatedContent.isEmpty()) {
            Toast.makeText(this, "수정할 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedCommentData = mapOf("content" to updatedContent)

        Log.d("EditCommentActivity", "Attempting to update comment with ID: $commentId")

        CoroutineScope(Dispatchers.IO).launch {
            val success = firestoreService.updateComment(commentId!!, updatedCommentData)
            withContext(Dispatchers.Main) {
                if (success) {
                    Log.d("EditCommentActivity", "Comment updated successfully")
                    Toast.makeText(this@EditComment, "댓글이 수정되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                    finish() // 수정 후 액티비티 종료
                } else {
                    Log.e("EditCommentActivity", "Failed to update comment")
                    Toast.makeText(this@EditComment, "댓글 수정에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

