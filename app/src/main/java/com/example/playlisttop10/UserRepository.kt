package com.example.playlisttop10

import com.google.firebase.firestore.FirebaseFirestore
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
                currUser = User(user.id, user.password, documentSnapshot.get("name").toString())

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

    suspend fun tryRegisterSong(song: Song): Result<String>{
        val db = FirebaseFirestore.getInstance()

        val songMap = hashMapOf<String, String>("title" to song.title, "singer" to song.singer, "album" to song.album)

        return try{
            db.collection("song")
                .document(song.title)
                .set(songMap)
                .await()
            Result.success("Success")
        }catch (e: Exception){
            Result.failure(Exception("fail to register song"))
        }
    }
}