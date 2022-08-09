package com.example.playlisttop10

import android.util.Log
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

    fun tryLogIn(id: String, password: String, callback: (result: Result<String>) -> Unit){
        db.collection("user")
            .document(id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    if (it.result.get("password") == password)
                        callback.invoke(Result.success("success"))
                    else
                        callback.invoke(Result.failure(Exception("mismatch")))
                }
                else{
                    callback.invoke(Result.failure(Exception("fail to connect")))
                }
            }
    }

    fun tryGetName(id: String, callback: (result: Result<Any?>) -> Unit){
        db.collection("user")
            .document(id)
            .get()
            .addOnSuccessListener{
                callback.invoke(Result.success(it.get("name")))
            }
            .addOnFailureListener{
                callback.invoke(Result.failure(it))
            }
    }
}