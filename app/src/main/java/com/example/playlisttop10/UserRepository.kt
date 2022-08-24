package com.example.playlisttop10

import android.util.Log
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
                currUser = User(id, password, documentSnapshot.get("name").toString(), mutableListOf())

                try{
                    val playlist = documentSnapshot.get("playlist") as List<*>

                    for (song in playlist){
                        song as HashMap<*, *>
                        currUser!!.playlist.add(Song(song["title"].toString(), song["singer"].toString(), song["album"].toString()))
                    }
                }catch (e: Exception){
                    Log.d("DEBUG", "tryLogIn: ${e.message}")
                    currUser!!.playlist = mutableListOf()
                }

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

    suspend fun tryRegisterMySong(song: Song): Result<String>{
        if (!doesSongExistInSongList(song)){
            return Result.failure(Exception("this song is already registered"))
        }

        val db = FirebaseFirestore.getInstance()
        val newSongListMap: MutableList<Song> = mutableListOf()

        if (currUser!!.playlist.isNotEmpty()){
            newSongListMap.addAll(currUser!!.playlist)
        }

        newSongListMap.add(song)
        currUser!!.playlist = newSongListMap

        val keyMap = hashMapOf("playlist" to newSongListMap)

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

    private fun doesSongExistInSongList(song: Song): Boolean{
        return song !in currUser!!.playlist
    }

    suspend fun updateSongInformation(oldSong: Song, newSong: Song): Result<String>{
        val db = FirebaseFirestore.getInstance()

        for (tmp in currUser!!.playlist){
            if (tmp == oldSong){
                tmp.singer = newSong.singer
                tmp.album = newSong.album
            }
        }

        return try{
            db.collection("user")
                .document(currUser!!.id)
                .update("playlist", currUser!!.playlist)
                .await()
            Result.success("success")
        }catch (e: Exception){
            Result.failure(Exception("fail to update song"))
        }
    }
}