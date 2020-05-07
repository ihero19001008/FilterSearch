package com.ankit.filtersearch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankit.filtersearch.R
import com.ankit.filtersearch.model.Post
import kotlinx.android.synthetic.main.rcl_item_post.view.*

class SimpleRecyclerAdapter(private val context: Context,private val posts: List<Post>) :
    RecyclerView.Adapter<SimpleRecyclerAdapter.PostHolder>() {

    class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post) {
            itemView.titleView.text = post.title
            itemView.contentView.text = post.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        return PostHolder(LayoutInflater.from(context).inflate(R.layout.rcl_item_post, parent, false))
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind(posts[position])
    }
}