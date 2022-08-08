package com.example.playlisttop10

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun trySignup(
        id: String,
        password: String,
        name: String,
        callback: (result: Result<String>) -> Unit
    ) {
        val user = hashMapOf<String, String>("id" to id, "password" to password, "name" to name)

        db.collection("user")
            .document(id)
            .set(user)
            .addOnSuccessListener {
                callback.invoke(Result.success("success"))
            }
            .addOnFailureListener {
                callback.invoke(Result.failure(it))
            }
    }

    fun tryGetIdList(callback: (result: Result<List<String>>) -> Unit)  {
        db.collection("user")
            .get()
            .addOnSuccessListener { documents ->
                callback.invoke(Result.success(documents.map { it.id }))
            }
            .addOnFailureListener{
                callback.invoke(Result.failure(it))
            }
    }
}