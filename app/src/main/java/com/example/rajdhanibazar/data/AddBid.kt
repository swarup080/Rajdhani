package com.example.rajdhanibazar.data

data class AddBid(
    val marketname: String?,
    val gamename: String?,
    val digit: String?,
    val amount: String?,
    val date: String?,
    val session: String?,
    val username: String?,
    val contact: String?,
    val email: String?
)
data class BidResponse(
    val status: String,
    val message: String,
    val bid: Boolean
)