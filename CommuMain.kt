package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.FirestoreService
import com.example.myapplication.data.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommuMain : AppCompatActivity() {
    private lateinit var popularPostsAdapter: PostAdapter
    private lateinit var popularPostsRecyclerView: RecyclerView
    private val firestoreService = FirestoreService()
    lateinit var btnBlog1: ImageButton
    lateinit var btnBlog2: ImageButton
    lateinit var btnBlog3: ImageButton
    lateinit var btnBlog4: ImageButton
    lateinit var btnViewAllPosts: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.commu_main)

        popularPostsRecyclerView = findViewById(R.id.recyclerViewPopularPosts)
        popularPostsRecyclerView.layoutManager = LinearLayoutManager(this)
        setupRecyclerView()

        btnBlog1 = findViewById(R.id.BtnBlog1)
        btnBlog2 = findViewById(R.id.BtnBlog2)
        btnBlog3 = findViewById(R.id.BtnBlog3)
        btnBlog4 = findViewById(R.id.BtnBlog4)
        btnViewAllPosts = findViewById(R.id.btnViewAllPosts)

        // "전체 글 보기" 버튼 클릭 시 Viewfull Activity로 이동
        btnViewAllPosts.setOnClickListener {
            val intent = Intent(this, Viewfull::class.java)
            startActivity(intent)
        }

        // 각 블로그 버튼에 클릭 리스너 추가
        btnBlog1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/kecoprumy/223506566088"))
            startActivity(intent)
        }

        btnBlog2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/kea_sese/222885788049"))
            startActivity(intent)
        }

        btnBlog3.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/moduenergy/222950594638"))
            startActivity(intent)
        }

        btnBlog4.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.greened.kr/"))
            startActivity(intent)
        }

        loadPopularPosts()
    }

    private fun setupRecyclerView() {
        popularPostsAdapter = PostAdapter(
            emptyList(),
            { post -> editPost(post) },  // 수정 클릭 시 호출
            { post -> deletePost(post) }  // 삭제 클릭 시 호출
        )
        popularPostsRecyclerView.adapter = popularPostsAdapter
    }

    private fun loadPopularPosts() {
        CoroutineScope(Dispatchers.IO).launch {
            val topPosts = firestoreService.getTopPosts()
            withContext(Dispatchers.Main) {
                popularPostsAdapter.updatePosts(topPosts)
            }
        }
    }

    private fun editPost(post: Post) {
        val intent = Intent(this, EditPost::class.java)
        intent.putExtra("postId", post.id)
        startActivity(intent)
    }

    private fun deletePost(post: Post) {
        CoroutineScope(Dispatchers.IO).launch {
            val success = firestoreService.deletePostWithComments(post.id)
            withContext(Dispatchers.Main) {
                if (success) {
                    loadPopularPosts() // 삭제 후 목록을 갱신
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadPopularPosts() // 액티비티가 다시 활성화될 때 최신 데이터를 가져옴
    }
}
