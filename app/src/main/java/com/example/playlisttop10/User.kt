package com.example.playlisttop10

data class User(
    var id: String = "",
    var password: String = "",
    var name: String = "",
    var songList: List<Song> = listOf()
)