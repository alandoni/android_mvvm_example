package com.daitan.example.socialnetwork.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daitan.example.socialnetwork.R
import com.daitan.example.socialnetwork.databinding.PostItemBinding
import com.daitan.example.socialnetwork.model.post.Post
import java.text.SimpleDateFormat
import java.util.*

class PostsAdapter: RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    var items: List<Post> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostItemBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class PostViewHolder(private val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.post = post
            binding.date.text = getTextDate(post.date, binding.date.context)
        }

        private fun getTextDate(date: Date, context: Context?): CharSequence? {
            val now = Date()
            val diffMilliseconds = now.time - date.time
            return context?.let {
                when {
                    diffMilliseconds < 60 * 1000 ->
                        it.getString(R.string.seconds_ago)
                    diffMilliseconds < 60 * 60 * 1000 -> {
                        val minutes = (diffMilliseconds / (60 * 1000)).toInt()
                        it.resources.getQuantityString(R.plurals.minutes_ago, minutes, minutes)
                    }
                    else -> {
                        val format = SimpleDateFormat(it.getString(R.string.date_format), Locale.getDefault())
                        return format.format(date)
                    }
                }
            }
        }
    }
}