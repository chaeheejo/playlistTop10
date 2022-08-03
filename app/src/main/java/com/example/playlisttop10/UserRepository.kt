package com.example.playlisttop10

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    public fun trySignup(id: String, password: String, name: String, callback: UserRepositoryCallback){
        val user = hashMapOf<String, String>("id" to id, "password" to password, "name" to name)

        db.collection("user")
            .document(id)
            .set(user)
            .addOnSuccessListener{
                callback.onComplete(Result.Success<String>("success"))
            }
            .addOnFailureListener{
                callback.onComplete(Result.Error(it))
            }
    }

    public interface UserRepositoryCallback {
        fun onComplete(result: Result<Any>)
    }
}