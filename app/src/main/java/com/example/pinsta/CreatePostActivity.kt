package com.example.pinsta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pinsta.daos.PostDao
import com.example.pinsta.databinding.ActivityCreatePostBinding

class CreatePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePostBinding
    private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postDao=PostDao()

        binding.postButton.setOnClickListener{
            val input=binding.postInput.text.toString().trim()
            if(input.isNotEmpty()) {
                postDao.addPost(input)
                finish()
            }
        }
        setUpRecyclerView()
    }
    private fun setUpRecyclerView() {

    }
}