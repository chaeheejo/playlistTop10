package com.example.playlisttop10

data class User(
    var id: String = "",
    var password: String = "",
    var name: String = "",
    var playlist: MutableList<Song> = mutableListOf(),
    var like: Int = 0
)