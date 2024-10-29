package com.example.rajdhanibazar.data


// Define the request body data class
data class HalfSangamData(
    val date: String,
    val session: String,
    val market_name: String,
    val game_name: String,
    val username: String,
    val contact: String,
    val email: String,
    val open_digits: String,
    val close_digits: String?,
    val open_pana: String?,
    val close_pana: String,
    val points: String
)

// Define the response data class
data class HalfSangamResponse(
    val status: String,
    val message: String,
    val data: HalfSangamData
)
