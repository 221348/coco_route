package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.data.FirestoreService
import com.example.myapplication.data.Comment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostDetail : AppCompatActivity() {

    private lateinit var commentAdapter: CommentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextComment: EditText
    private lateinit var checkBoxAnonymous: CheckBox
    private lateinit var buttonSubmitComment: ImageButton
    private lateinit var textViewTitle: TextView
    private lateinit var textViewContent: TextView
    private lateinit var textViewRecommendCount: TextView
    private lateinit var buttonRecommend: Button
    private lateinit var menuButton: ImageButton
    private val firestoreService = FirestoreService()
    private lateinit var textViewDate: TextView
    private lateinit var textViewCommentCount: TextView
    private var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_detail)

        postId = intent.getStringExtra("postId") ?: ""

        recyclerView = findViewById(R.id.recyclerViewComments)
        recyclerView.layoutManager = LinearLayoutManager(this)
        editTextComment = findViewById(R.id.editTextComment)
        checkBoxAnonymous = findViewById(R.id.checkBoxAnonymous)
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment)
        textViewTitle = findViewById(R.id.textViewTitle)
        textViewContent = findViewById(R.id.textViewContent)
        textViewRecommendCount = findViewById(R.id.textViewRecommendCount)
        buttonRecommend = findViewById(R.id.buttonRecommend)
        menuButton = findViewById(R.id.menuButton)
        textViewDate = findViewById(R.id.textViewDate)
        textViewCommentCount = findViewById(R.id.textViewCommentCount)


        // CommentAdapter 초기화
        commentAdapter = CommentAdapter(
            comments = emptyList(),
            onEditClicked = { comment ->
                val intent = Intent(this, EditComment::class.java)
                intent.putExtra("commentId", comment.id)
                intent.putExtra("commentContent", comment.content)
                startActivity(intent)
            },
            onDeleteClicked = { comment ->
                deleteComment(comment.id)
            }
        )
        recyclerView.adapter = commentAdapter

        loadPostDetails()
        loadComments()

        buttonSubmitComment.setOnClickListener {
            submitComment()
        }

        buttonRecommend.setOnClickListener {
            toggleRecommend()
        }

        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    private fun loadPostDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            val post = firestoreService.getPostById(postId)
            withContext(Dispatchers.Main) {
                post?.let {
                    textViewTitle.text = it.title
                    textViewContent.text = it.content
                    textViewRecommendCount.text = it.recommendCount.toString()

                    // Timestamp를 문자열로 변환
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val dateString = sdf.format(it.date.toDate()) // Timestamp를 Date로 변환 후 포맷 적용
                    textViewDate.text = dateString // 변환된 문자열을 TextView에 설정
                }
            }
        }
    }

    private fun loadComments() {
        CoroutineScope(Dispatchers.IO).launch {
            val comments = firestoreService.getAllComments().filter { it.postId == postId }
            withContext(Dispatchers.Main) {
                commentAdapter.updateComments(comments)
                textViewCommentCount.text = comments.size.toString()
            }
        }
    }

    private fun submitComment() {
        val commentText = editTextComment.text.toString()
        if (commentText.isEmpty()) {
            Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val user = if (checkBoxAnonymous.isChecked) "익명" else "사용자"
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val comment = Comment(postId = postId, user = user, content = commentText, date = currentDate)

        CoroutineScope(Dispatchers.IO).launch {
            val commentId = firestoreService.insertComment(comment)
            if (commentId != null) {
                firestoreService.updateComment(commentId, mapOf("id" to commentId))
                withContext(Dispatchers.Main) {
                    loadComments()
                    editTextComment.text.clear()
                    Toast.makeText(this@PostDetail, "댓글이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PostDetail, "댓글 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun toggleRecommend() {
        CoroutineScope(Dispatchers.IO).launch {
            val userId = getCurrentUserId() // 현재 사용자의 UID를 가져오는 함수
            val success = firestoreService.toggleRecommendPost(postId, userId)
            withContext(Dispatchers.Main) {
                if (success) {
                    loadComments()
                    loadPostDetails() // 추천 상태 변경 후 게시물 정보 갱신
                    Toast.makeText(this@PostDetail, "추천 상태가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@PostDetail, "추천 상태 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_post_options, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit -> {
                    val intent = Intent(this, EditPost::class.java)
                    intent.putExtra("postId", postId)
                    startActivity(intent)
                    true
                }
                R.id.action_delete -> {
                    deletePostWithComments(postId) // postId 전달
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun deletePostWithComments(postId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val success = firestoreService.deletePostWithComments(postId)
            withContext(Dispatchers.Main) {
                if (success) {
                    Toast.makeText(this@PostDetail, "게시물과 관련 댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@PostDetail, "게시물 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteComment(commentId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val success = firestoreService.deleteComment(commentId)
            withContext(Dispatchers.Main) {
                if (success) {
                    Toast.makeText(this@PostDetail, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    loadComments()
                } else {
                    Toast.makeText(this@PostDetail, "댓글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentUserId(): String {
        // Firebase Auth 또는 다른 방법으로 현재 사용자의 ID를 가져오는 함수
        // 이 예제에서는 "userId"라는 더미 값으로 반환
        return "userId"
    }
}

