package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.data.FirestoreService
import com.example.myapplication.data.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Viewfull : AppCompatActivity() {
    lateinit var btnNewPost: Button
    lateinit var recyclerView: RecyclerView
    private val posts = mutableListOf<Post>()
    private val firestoreService = FirestoreService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_view)

        recyclerView = findViewById(R.id.recyclerViewPosts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PostAdapter(
            posts,
            this::onEditClicked,
            this::onDeleteClicked
        )

        loadPosts()

        btnNewPost = findViewById(R.id.BtnNewPost)
        btnNewPost.setOnClickListener {
            val intent = Intent(this, NewPost::class.java)
            startActivityForResult(intent, NEW_POST_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_POST_REQUEST_CODE && resultCode == RESULT_OK) {
            loadPosts()  // 글이 작성된 후 전체 글을 다시 불러옵니다.
        }
    }

    private fun loadPosts() {
        CoroutineScope(Dispatchers.IO).launch {
            val postsFromDb = firestoreService.getAllPosts()
            withContext(Dispatchers.Main) {
                posts.clear()
                posts.addAll(postsFromDb)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun onEditClicked(post: Post) {
        val intent = Intent(this, EditPost::class.java)
        intent.putExtra("postId", post.id)
        startActivity(intent)
    }

    private fun onDeleteClicked(post: Post) {
        CoroutineScope(Dispatchers.IO).launch {
            val success = firestoreService.deletePostWithComments(post.id)
            withContext(Dispatchers.Main) {
                if (success) {
                    posts.remove(post)
                    recyclerView.adapter?.notifyDataSetChanged()
                    Toast.makeText(this@Viewfull, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Viewfull, "게시물 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val NEW_POST_REQUEST_CODE = 1
    }
}


