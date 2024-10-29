package com.example.rajdhanibazar.data

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a market entity.
 *
 * @property marketImage Resource ID for the market image.
 * @property openTime The opening time of the market (e.g., "09:00 AM").
 * @property closeTime The closing time of the market (e.g., "05:00 PM").
 * @property marketName The name of the market.
 * @property result Result code or value related to the market.
 * @property marketStatus The current status of the market (e.g., "Open", "Closed").
 */
data class Market(
    val marketImage: Int,
    @SerializedName("start_time") val openTime: String,
    @SerializedName("close_time") val closeTime: String,
    @SerializedName("name") val marketName: String,
    @SerializedName("monday") val monday: String,
    @SerializedName("tuesday") val tuesday: String,
    @SerializedName("wednesday") val wednesday: String,
    @SerializedName("thursday") val thursday: String,
    @SerializedName("friday") val friday: String,
    @SerializedName("saturday") val saturday: String,
    @SerializedName("sunday") val sunday: String,
    @SerializedName("result") val result: String,
)