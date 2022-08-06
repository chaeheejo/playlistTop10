package com.example.playlisttop10

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
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

    suspend fun getIdList(): List<String> {
        var idList: List<String> = arrayListOf()

        db.collection("user")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("debug", "trySignup: "+1+idList.size)
                idList = documents.map { it.id }
                Log.d("debug", "trySignup: "+2+idList.size)
            }
            .await()
        Log.d("debug", "trySignup: "+6+idList.size)
        return idList
    }
}