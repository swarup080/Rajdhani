package com.example.rajdhanibazar.data

import com.google.gson.annotations.SerializedName

data class GameResult(
    @SerializedName("market") val market: String,
    @SerializedName("date") val date: String,
    @SerializedName("session") val session: String,
    @SerializedName("open_pana") val openPana: String,
    @SerializedName("open_result") val openResult: String,
    @SerializedName("close_pana") val closePana: String,
    @SerializedName("close_result") val closeResult: String
)
