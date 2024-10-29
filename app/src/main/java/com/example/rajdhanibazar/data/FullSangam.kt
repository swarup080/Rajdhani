package com.example.rajdhanibazar.data

data class FullSangamResponse(
    val status: String,
    val message: String,
    val data: FullSangamData
)

data class FullSangamData(
    val date: String,
    val market_name: String,
    val game_name: String,
    val username: String,
    val contact: String,
    val email: String,
    val open_pana: String,
    val close_pana: String,
    val points: String,
)
