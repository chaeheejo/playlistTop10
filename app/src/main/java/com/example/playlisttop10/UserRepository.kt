package com.example.playlisttop10

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private var currUser: User? = null

    suspend fun trySignUp(id: String, password: String, name: String): Result<String> {
        if (doesDuplicateIdExist(id))
            return Result.failure(Exception("Id Already Exists"))

        val user = hashMapOf<String, String>("id" to id, "password" to password, "name" to name)
        return try {
            db.collection("user")
                .document(id)
                .set(user)
                .await()
            Result.success("Success")
        } catch (e: Exception) {
            Result.failure(Exception("network error"))
        }
    }

    suspend fun tryLogIn(id: String, password: String): Result<String>{
        return try {
            val documentSnapshot = db.collection("user")
                .document(id)
                .get()
                .await()

            if (documentSnapshot.get("password") == password) {
                Result.success("success")
            } else {
                Result.failure(Exception("password does not match"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("id does not exist"))
        }
    }

    private suspend fun doesDuplicateIdExist(id: String): Boolean {
        val documentSnapshot = db.collection("user")
            .whereEqualTo("id", id)
            .get()
            .await()
            .documents
        return documentSnapshot.isNotEmpty()
    }
}