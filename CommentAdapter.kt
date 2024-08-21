package com.example.myapplication

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Comment

class CommentAdapter(
    private var comments: List<Comment>,
    private val onEditClicked: (Comment) -> Unit,
    private val onDeleteClicked: (Comment) -> Unit
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int = comments.size

    fun updateComments(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userTextView: TextView = itemView.findViewById(R.id.commentUser)
        private val contentTextView: TextView = itemView.findViewById(R.id.commentText)
        private val dateTextView: TextView = itemView.findViewById(R.id.commentDate)
        private val recommendCountTextView: TextView = itemView.findViewById(R.id.commentRecommendCount)
        private val commentMenuButton: ImageButton = itemView.findViewById(R.id.commentMenuButton)

        fun bind(comment: Comment) {
            userTextView.text = comment.user
            contentTextView.text = comment.content
            dateTextView.text = comment.date
            recommendCountTextView.text = comment.recommendCount.toString()

            commentMenuButton.setOnClickListener { view ->
                showPopupMenu(view, comment)
            }
        }

        private fun showPopupMenu(view: View, comment: Comment) {
            val popup = PopupMenu(view.context, view)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_comment_options, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit_comment -> {
                        onEditClicked(comment)
                        true
                    }
                    R.id.action_delete_comment -> {
                        onDeleteClicked(comment)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }
}
