package com.daitan.example.socialnetwork.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.daitan.example.socialnetwork.databinding.ActivityMainBinding
import com.daitan.example.socialnetwork.model.post.Post

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter()
        binding.postsList.adapter = adapter
        binding.postsList.layoutManager = LinearLayoutManager(this)

        adapter.items = listOf(Post(user = "User", text = "Texto de exemplo"))
    }
}