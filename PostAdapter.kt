package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Post

class PostAdapter(
    private var posts: List<Post>,
    private val onEditClicked: (Post) -> Unit,
    private val onDeleteClicked: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val recommendCountTextView: TextView =
            itemView.findViewById(R.id.textViewRecommendCount)

        fun bind(post: Post) {
            titleTextView.text = post.title
            recommendCountTextView.text = "추천수: ${post.recommendCount}"

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PostDetail::class.java)
                intent.putExtra("postId", post.id)
                intent.putExtra("postTitle", post.title)
                intent.putExtra("postContent", post.content)
                itemView.context.startActivity(intent)
            }
        }
    }
}
