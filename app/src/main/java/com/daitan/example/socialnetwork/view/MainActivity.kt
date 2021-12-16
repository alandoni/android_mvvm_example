package com.daitan.example.socialnetwork.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.daitan.example.socialnetwork.databinding.ActivityMainBinding
import com.daitan.example.socialnetwork.model.post.Post
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val adapter = PostsAdapter()
        binding.postsList.adapter = adapter
        binding.postsList.layoutManager = LinearLayoutManager(this)

        binding.sendButton.setOnClickListener {
            viewModel.createPost()
        }

        viewModel.posts.observe(this) {
            adapter.items = it
        }
    }
}