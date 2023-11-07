package com.example.pinsta.daos

import com.example.pinsta.models.Post
import com.example.pinsta.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db=FirebaseFirestore.getInstance()
    val postcollection=db.collection("posts")
    val auth=Firebase.auth

    fun addPost(text:String){
        GlobalScope.launch {
            val currentuserId= auth.currentUser!!.uid
            val userDao=userDao()
            val user=userDao.getUserById(currentuserId).await().toObject(User::class.java)!!

            val currentTime=System.currentTimeMillis()
            val post= Post(text,user,currentTime)
            postcollection.document().set(post)
        }
    }

    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postcollection.document(postId).get()
    }

    fun updateLikes(postId: String){

        GlobalScope.launch {
            val currentUserId=auth.currentUser!!.uid
            val post=getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked= post.likedBy.contains(currentUserId)

            if(isLiked){
                post.likedBy.remove(currentUserId)
            }
            else{
                post.likedBy.add(currentUserId)
            }
            postcollection.document(postId).set(post)
        }
    }
}