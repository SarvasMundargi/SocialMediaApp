package com.example.pinsta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinsta.daos.PostDao
import com.example.pinsta.databinding.ActivityMainBinding
import com.example.pinsta.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), PostAdapter.IPostAdapter {
    private lateinit var binding: ActivityMainBinding
    private lateinit var postDao: PostDao
    private lateinit var adapter: PostAdapter
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpDrawerLayout()
        binding.fab.setOnClickListener {
            startActivity(Intent(this,CreatePostActivity::class.java))
        }
        setUpRecyclerView()
    }

    private fun setUpDrawerLayout() {
        val drawerlayout: DrawerLayout=findViewById(R.id.drawer_layout)
        val nav_view: NavigationView=findViewById(R.id.nav_view)

        toggle= ActionBarDrawerToggle(this,drawerlayout,R.string.open,R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val auth=Firebase.auth

        nav_view.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.logout->{
                    auth.signOut()
                    startActivity(Intent(this,SignInActivity::class.java))
                    finish()
                }
            }

            true
        }
    }

    private fun setUpRecyclerView() {
        postDao = PostDao()
        val postsCollections = postDao.postcollection
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}