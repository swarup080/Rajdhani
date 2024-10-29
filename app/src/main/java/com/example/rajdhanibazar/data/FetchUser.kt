package com.example.rajdhanibazar.data

data class FetchUser(
    val name: String,
    val number: String,
    val email: String,
    val balance: Int
)
data class FetchUserResponse(
    val success: Boolean,
    val message: String,
    val data: List<FetchUser>
)
