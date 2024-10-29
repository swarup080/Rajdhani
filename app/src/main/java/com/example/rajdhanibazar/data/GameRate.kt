package com.example.rajdhanibazar.data

import com.google.gson.annotations.SerializedName

class GameRate (
    @SerializedName("name") val name: String,
    @SerializedName("rate") val rate: String
)