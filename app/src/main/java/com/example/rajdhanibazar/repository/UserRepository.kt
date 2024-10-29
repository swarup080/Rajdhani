package com.example.rajdhanibazar.repository

import android.util.Log
import com.example.rajdhanibazar.data.FetchUser
import com.example.rajdhanibazar.data.FetchUserResponse
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {


    fun getUserByPhoneNumber(phoneNumber: String, callback: (FetchUser?) -> Unit) {
        RetrofitInstance.apiInterface.getUsers().enqueue(object : Callback<FetchUserResponse> {
            override fun onResponse(call: Call<FetchUserResponse>, response: Response<FetchUserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    val user = userResponse?.data?.find { it.number == phoneNumber }
                    Log.d("UserRepository", "User found: $user")
                    callback(user)
                } else {
                    Log.e("UserRepository", "Response error: ${response.message()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<FetchUserResponse>, t: Throwable) {
                Log.e("UserRepository", "API call failed", t)
                callback(null)
            }
        })
    }

}
