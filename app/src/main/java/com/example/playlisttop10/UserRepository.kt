package com.example.playlisttop10

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object UserRepository {
    var currUser: User ?= null

    suspend fun trySignUp(user: User): Result<String> {
        val db = FirebaseFirestore.getInstance()

        if (doesDuplicateIdExist(user.id))
            return Result.failure(Exception("Id Already Exists"))

        val userMap = hashMapOf<String, String>("id" to user.id, "password" to user.password, "name" to user.name)

        return try {
            db.collection("user")
                .document(user.id)
                .set(userMap)
                .await()
            Result.success("Success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to sign up"))
        }
    }

    suspend fun tryLogIn(user: User): Result<String> {
        val db = FirebaseFirestore.getInstance()

        return try {
            val documentSnapshot = db.collection("user")
                .document(user.id)
                .get()
                .await()

            if (documentSnapshot.get("password") == user.password) {
                currUser = User(user.id, user.password, documentSnapshot.get("name").toString(), arrayListOf())

                Result.success("success")
            } else {
                Result.failure(Exception("password does not match"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("id does not exist"))
        }
    }

    private suspend fun doesDuplicateIdExist(id: String): Boolean {
        val db = FirebaseFirestore.getInstance()
        val documentSnapshot = db.collection("user")
            .whereEqualTo("id", id)
            .get()
            .await()
            .documents
        return documentSnapshot.isNotEmpty()
    }

    suspend fun tryRegisterSongTitle(title: String): Result<String>{
        val db = FirebaseFirestore.getInstance()

        currUser!!.playlist.add(title)

        val keyMap = hashMapOf("playlist" to currUser!!.playlist)

        return try {
            db.collection("user")
                .document(currUser!!.id)
                .set(keyMap, SetOptions.merge())
                .await()
            Result.success("success")
        }catch (e: Exception){
            Result.failure(Exception("fail to register song title"))
        }
    }
}