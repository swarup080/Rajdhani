package com.example.rajdhanibazar.authActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.activity.MainActivity
import com.example.rajdhanibazar.data.LoginRequest
import com.example.rajdhanibazar.data.LoginResponse
import com.example.rajdhanibazar.databinding.ActivityLoginBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)

        // Check if the user is already logged in
        val savedToken = sharedPreferences.getString("token", null)
        if (savedToken != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // dont have an account
        binding.noAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SignUpActivity::class.java))
        }
        // When user clicks SignIn button
        binding.signIn.setOnClickListener {
            phoneNumber = binding.phoneNumber.text.toString()
            val password = binding.password.text.toString()

            if (phoneNumber.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            } else {
                val loginRequest = LoginRequest(phoneNumber, password)
                postCheckUserData(loginRequest)
            }
        }

        // forgot password
        binding.forgotPassword.setOnClickListener {
         showForgotPasswordDialog()
        }
    }

    // this function perform server request
    private fun postCheckUserData(loginRequest: LoginRequest) {
        RetrofitInstance.apiInterface.login(loginRequest)
            .enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.status == "true") {
                            // Handle successful login
                            Toast.makeText(
                                this@LoginActivity,
                                loginResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            // Save the token for future requests
                            val editor = sharedPreferences.edit()
                            editor.putString("token", loginResponse.token)
                            editor.putString("tokenType", loginResponse.tokenType)
                            editor.apply()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("phoneNumber",phoneNumber)
                            startActivity(intent)
                            finish() // Ensure the login activity is closed
                        } else {
                            // Handle error in response
                            Toast.makeText(
                                this@LoginActivity,
                                "Login failed: ${loginResponse?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle unsuccessful response
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    // Handle failure
                    Toast.makeText(this@LoginActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
    private fun showForgotPasswordDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.forgot_password_dialog, null)
        val emailEditText = dialogView.findViewById<EditText>(R.id.emailEditText)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.submitButton).setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                // Handle the email submission (e.g., send a password reset request)
                forgotPassword(email)
                dialog.dismiss()
            }
        }

        dialog.show()
    }
    private fun forgotPassword(email: String) {
        val apiService = RetrofitInstance.apiInterface

        apiService.forgotPassword(email).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Check your email for password reset instructions", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Failed to send reset instructions", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}