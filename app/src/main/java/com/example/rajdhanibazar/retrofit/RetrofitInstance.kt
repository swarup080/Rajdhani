package com.example.rajdhanibazar.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://rajdhanimarketmatka.in/api/"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val apiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}