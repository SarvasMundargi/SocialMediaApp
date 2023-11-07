package com.example.pinsta.daos

import com.example.pinsta.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class userDao {
    private val db=FirebaseFirestore.getInstance()
    private val usercollection=db.collection("users")

    fun adduser(user: User?){
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                usercollection.document(user.uid).set(it)
            }
        }
    }

    fun getUserById(uId:String) : Task<DocumentSnapshot> {
        return usercollection.document(uId).get()
    }
}