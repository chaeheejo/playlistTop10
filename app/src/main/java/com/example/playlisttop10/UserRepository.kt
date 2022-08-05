package com.example.playlisttop10

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

    suspend fun getIdList(): MutableList<String> {
        val idList: MutableList<String> = arrayListOf()
        Log.d("DEBUG ", "trySignup: " + 3 + " " + idList.size)
        Log.d("DEBUG ", "trySignup: " + 4 + " " + idList.size)
        db.collection("user")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    idList.add(document.id)
                    Log.d("DEBUG ", "trySignup: " + 99 + " " + idList.size)
                }
            }
            .await()
        Log.d("DEBUG ", "trySignup: " + 5 + " " + idList.size)
        return idList
    }
}