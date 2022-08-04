package com.example.playlisttop10

import com.google.firebase.firestore.FirebaseFirestore

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

    fun getUerList(): MutableList<String> {
        val userIdList: MutableList<String> = arrayListOf()

        db.collection("user")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    userIdList.add(document.id)
                }
            }

        return userIdList
    }
}