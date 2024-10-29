package com.example.rajdhanibazar.data

data class Withdraw(
    val name: String,
    val number: String,
    val balance: String,
    val amount: String,
    val date: String,

    val phonepe: String,
    val google_pay: String,
    val paytm: String
)
data class WithdrawResponse(
    val status: String,
    val message: String,
    val withdrawal_request: Withdraw?
)

