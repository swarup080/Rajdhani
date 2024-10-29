package com.example.rajdhanibazar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rajdhanibazar.data.FetchUser
import com.example.rajdhanibazar.repository.UserRepository

class UserViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val _user = MutableLiveData<FetchUser?>()
    val user: LiveData<FetchUser?> get() = _user



    fun fetchUser(phoneNumber: String) {
        Log.d("UserViewModel", "Fetching user with phone number: $phoneNumber")
        userRepository.getUserByPhoneNumber(phoneNumber) { user ->
            if (user != null) {
                Log.d("UserViewModel", "User fetched successfully: $user")
            } else {
                Log.d("UserViewModel", "User not found or error occurred")
            }
            _user.postValue(user)
        }
    }
}
