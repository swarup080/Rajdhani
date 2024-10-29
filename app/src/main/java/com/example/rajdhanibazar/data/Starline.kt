package com.example.rajdhanibazar.data

data class StarlineGamesResponse(
    val success: Boolean,
    val message: String,
    val data: List<StarlineGame>
)

data class StarlineGame(
    val time: String,
    val result: String,
    val status: String
)
