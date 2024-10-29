package com.example.rajdhanibazar.data

import com.google.gson.annotations.SerializedName

data class Gali(
    val id: Int,
    @SerializedName("time") val time: String,
    @SerializedName("result") val result: String,
    @SerializedName("status") val marketStatus: String,
    @SerializedName("name") val name: String
)
data class GaliResponse(
    val success: Boolean,
    val message: String,
    val data: List<Gali>
)
