package com.example.rajdhanibazar.data

data class SignUpRequest(
    val name: String,
    val number: String,
    val email: String,
    val password: String,
    val demoPassword: String,
    val balance: Int
)
data class SignUpResponse(
    val status: String,
    val error: String
)
data class LoginRequest(
    val number: String,
    val password: String
)
data class LoginResponse(
    val status: String,
    val message: String,
    val token: String,
    val tokenType: String
)


