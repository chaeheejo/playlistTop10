package com.example.playlisttop10

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object UserRepository {
    var currUser: User ?= null

    suspend fun trySignUp(id: String, password: String, name: String): Result<String> {
        val db = FirebaseFirestore.getInstance()

        if (doesDuplicateIdExist(id))
            return Result.failure(Exception("Id Already Exists"))

        val userMap = hashMapOf<String, String>("id" to id, "password" to password, "name" to name)

        return try {
            db.collection("user")
                .document(id)
                .set(userMap)
                .await()
            Result.success("Success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to sign up"))
        }
    }

    suspend fun tryLogIn(id: String, password: String): Result<String> {
        val db = FirebaseFirestore.getInstance()

        return try {
            val documentSnapshot = db.collection("user")
                .document(id)
                .get()
                .await()

            if (documentSnapshot.get("password") == password) {
                currUser = User(id, password, documentSnapshot.get("name").toString())

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

        val newKeyList: MutableList<String> = mutableListOf()
        newKeyList.addAll(currUser!!.titleListForPlaylist)
        newKeyList.add(title)
        currUser!!.titleListForPlaylist = newKeyList

        val keyMap = hashMapOf("playlist" to newKeyList)

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

    suspend fun setSongTitleForCurrUser(){
        val db = FirebaseFirestore.getInstance()

        val documentSnapshot = db.collection("user")
            .document(currUser!!.id)
            .get()
            .await()
        currUser!!.titleListForPlaylist = documentSnapshot.get("playlist") as List<String>
    }
}