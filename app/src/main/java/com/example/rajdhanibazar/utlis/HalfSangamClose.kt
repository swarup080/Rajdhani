package com.example.rajdhanibazar.utlis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.activity.MainActivity
import com.example.rajdhanibazar.data.Balance
import com.example.rajdhanibazar.data.HalfSangamData
import com.example.rajdhanibazar.data.HalfSangamResponse
import com.example.rajdhanibazar.databinding.ActivityHalfSangamCloseBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class HalfSangamClose : AppCompatActivity() {
    private lateinit var binding: ActivityHalfSangamCloseBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName: String
    private lateinit var email: String
    private var phoneNumber: String? = null
    private var marketName: String? = null
    private var gameName: String? = null
    private var session: String? = null
    private var gameDate: String? = null
    private var openPanna: String? = null
    private var closeDigit: String? = null
    private var gamePointAmount: String? = null
    private var enterBalance: Int = 0
    private var userBalance: Int = 0
    private var currentBalance: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHalfSangamCloseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeSharedPreferences()
        retrieveIntentData()
        setupDate()
        setupAddBidButtonListener()
    }

    private fun initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
        userName = sharedPreferences.getString("userName", "") ?: ""
        email = sharedPreferences.getString("userEmail", "") ?: ""
        phoneNumber = sharedPreferences.getString("phoneNumber", "")
        marketName = sharedPreferences.getString("marketName", "")
        userBalance = sharedPreferences.getInt("userBalance", 0)
        Log.d("HalfSangamOpen", "User Data: $userName, $email, $phoneNumber, $marketName, $userBalance")
    }

    private fun retrieveIntentData() {
        gameName = intent.getStringExtra("gameName") ?: ""
        session = intent.getStringExtra("selectedOption") ?: ""
        binding.gamePlayName.text = gameName
    }

    private fun setupDate() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        binding.gameDate.setText(currentDate)
        gameDate = binding.gameDate.text.toString()
    }

    private fun setupAddBidButtonListener() {
        binding.addBid.setOnClickListener {
            getPanna()
            getPoint()

            if (openPanna != null && closeDigit != null && enterBalance >= 10) {
                sendBidRequest()
            } else {
                Log.d("HalfSangamOpen", "Validation failed: openPanna, CloseDigit, or EnterBalance is incorrect.")
                Toast.makeText(this, "Invalid bid. Check Open Panna, Close Digit, and Balance.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendBidRequest() {
        val halfSangamRequest = gameDate?.let {
            HalfSangamData(
                date = it,
                session = session ?: "",
                market_name = marketName ?: "",
                game_name = gameName ?: "",
                username = userName,
                contact = phoneNumber ?: "",
                email = email,
                open_digits = null.toString(),
                close_digits = closeDigit?:"",
                open_pana = openPanna?:"",
                close_pana = null.toString(),
                points = enterBalance.toString()
            )
        }

        if (halfSangamRequest != null) {
            RetrofitInstance.apiInterface.storeHalfSangam(halfSangamRequest)
                .enqueue(object : Callback<HalfSangamResponse> {
                    override fun onResponse(call: Call<HalfSangamResponse>, response: Response<HalfSangamResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@HalfSangamClose, response.body()?.message ?: "Success", Toast.LENGTH_SHORT).show()
                            currentBalance = userBalance - enterBalance
                            updateUserBalanceAfterBid()
                            navigateToMainActivity()
                        } else {
                            Log.d("HalfSangamOpen", "API Response failed: ${response.errorBody()}")
                            Toast.makeText(this@HalfSangamClose, "Failed to store Half Sangam", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<HalfSangamResponse>, t: Throwable) {
                        Log.e("HalfSangamOpen", "API Request Failed: ${t.message}")
                        Toast.makeText(this@HalfSangamClose, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun getPanna() {
        val gameOpenPanna = binding.openPanna.text.toString().trim()
        val gameCloseDigit = binding.closePanna.text.toString().trim()

        if (gameOpenPanna.length != 3 || gameCloseDigit.length != 1) {
            Toast.makeText(this, "Invalid Panna", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val digit = gameOpenPanna.toInt()
            val digit2 = gameCloseDigit.toInt()

            if (digit in 111..999) {
                openPanna = gameOpenPanna
            } else {
                Toast.makeText(this, "Open Panna out of range (111-999)", Toast.LENGTH_SHORT).show()
                return
            }

            if (digit2 in 0..9) {
                closeDigit = gameCloseDigit
            } else {
                Toast.makeText(this, "Close Digit out of range (1-9)", Toast.LENGTH_SHORT).show()
                return
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPoint() {
        val gamePoint = binding.points.text.toString().trim()
        val point = gamePoint.toIntOrNull()

        if (point != null && point >= 10 && point <= userBalance) {
            enterBalance = point
        } else {
            Toast.makeText(this, "Invalid points. Minimum 10 points required.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserBalanceAfterBid() {
        val number = phoneNumber.toString()
        val balanceUpdate = Balance(currentBalance)

        RetrofitInstance.apiInterface.updateBalance(number, balanceUpdate)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string()
                        Toast.makeText(this@HalfSangamClose, responseBody, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this@HalfSangamClose,
                            "Failed to update balance",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@HalfSangamClose, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@HalfSangamClose, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}