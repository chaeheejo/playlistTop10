package com.example.playlisttop10

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object SongRepository {

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