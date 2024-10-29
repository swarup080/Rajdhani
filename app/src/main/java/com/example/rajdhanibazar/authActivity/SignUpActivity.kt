package com.example.rajdhanibazar.authActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.activity.MainActivity
import com.example.rajdhanibazar.data.SignUpRequest
import com.example.rajdhanibazar.data.SignUpResponse
import com.example.rajdhanibazar.databinding.ActivitySignUpBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var phone: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize sharedPreferences
        sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)

        // Check if the user is already signed up
//        val isSignedUp = sharedPreferences.getBoolean("isSignedUp", false)
//        if (isSignedUp) {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
        // already have an account
        binding.alreadyExist.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        // sign up
        signUp()
    }

    private fun signUp() {
        // when user click sign up button
        binding.signUp.setOnClickListener {
            val name = binding.name.text.toString()
            phone = binding.phoneNumber.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            // Assuming balance is hardcoded or obtained from elsewhere
            val balance = 0

            if (password == confirmPassword) {
                val signUpRequest = SignUpRequest(name, phone, email, password, password, balance)
                //This function handle user sign up request
                postUserData(signUpRequest)

            } else if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                Log.d("Signup", "Passwords do not match")
            }
        }
    }

    private fun postUserData(signUpRequest: SignUpRequest) {
        RetrofitInstance.apiInterface.signUp(signUpRequest)
            .enqueue(object : Callback<SignUpResponse?> {
                override fun onResponse(p0: Call<SignUpResponse?>, p1: Response<SignUpResponse?>) {
                    if (p1.isSuccessful) {
                        val signUpResponse = p1.body()
                        if (signUpResponse?.status == "true") {
                            // Handle successful sign-up
                            Toast.makeText(
                                this@SignUpActivity,
                                "Sign-up successful!",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Save the sign-up status in sharedPreferences
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isSignedUp", true)
                            editor.apply()

                            // Navigate to MainActivity
                            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            intent.putExtra("phoneNumber", phone)
                            startActivity(intent)
                            finish()
                        } else {
                            // Handle error in response
                            Toast.makeText(
                                this@SignUpActivity,
                                "Sign Up failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle unsuccessful response
                        Toast.makeText(
                            this@SignUpActivity,
                            "Error: Response Unsuccessful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(p0: Call<SignUpResponse?>, p1: Throwable) {

                }
            })
    }
}