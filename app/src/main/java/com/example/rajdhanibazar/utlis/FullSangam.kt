package com.example.rajdhanibazar.utlis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rajdhanibazar.activity.MainActivity
import com.example.rajdhanibazar.data.Balance
import com.example.rajdhanibazar.data.FullSangamData
import com.example.rajdhanibazar.data.FullSangamResponse
import com.example.rajdhanibazar.databinding.ActivityFullSangamBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class FullSangam : AppCompatActivity() {
    private lateinit var binding: ActivityFullSangamBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName: String
    private lateinit var email: String
    private var phoneNumber: String? = null

    private var gameName: String? = null
    private var marketName: String? = null
    private var gameDate: String? = null
    private var openPanna: String? = null
    private var closePanna: String? = null
    private var gamePointAmount: String? = null

    // Account balance
    private var enterBalance: Int = 0
    private var userBalance: Int = 0
    private var currentBalance: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullSangamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("FullSangam", "onCreate called")

        initializeSharedPreferences()
        retrieveIntentData()
        setupDate()
        setupAddBidButtonListener()
    }

    private fun initializeSharedPreferences() {
        Log.d("FullSangam", "Initializing SharedPreferences")
        sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
        userName = sharedPreferences.getString("userName", null).toString()
        email = sharedPreferences.getString("userEmail", null).toString()
        phoneNumber = sharedPreferences.getString("phoneNumber", null)
        marketName = sharedPreferences.getString("marketName", null)
        userBalance = sharedPreferences.getInt("userBalance", 0)

        Log.d(
            "FullSangam",
            "UserName: $userName, Email: $email, Phone: $phoneNumber, Market: $marketName, Balance: $userBalance"
        )
    }

    private fun retrieveIntentData() {
        gameName = intent.getStringExtra("gameName")
        Log.d("FullSangam", "GameName retrieved from Intent: $gameName")
        binding.gamePlayName.text = gameName
    }

    private fun setupDate() {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val currentDate: String = dateFormat.format(calendar.time)
        binding.gameDate.setText(currentDate)
        gameDate = binding.gameDate.text.toString()

        Log.d("FullSangam", "Current Date Set: $currentDate")
    }

    private fun setupAddBidButtonListener() {
        binding.addBid.setOnClickListener {
            Log.d("FullSangam", "Add Bid Button Clicked")
            getPanna()
            getPoint()

            Log.d(
                "FullSangam",
                "OpenPanna: $openPanna, ClosePanna: $closePanna, EnterBalance: $enterBalance"
            )

            if (openPanna != null && closePanna != null && enterBalance >= 10) {
                sendBidRequest()
            } else {
                Log.d("FullSangam", "Validation failed for OpenPanna or ClosePanna or EnterBalance")
            }
        }
    }

    private fun sendBidRequest() {
        val requestData = FullSangamData(
            date = gameDate ?: "",
            market_name = marketName ?: "",
            game_name = gameName ?: "",
            username = userName,
            contact = phoneNumber ?: "",
            email = email,
            open_pana = openPanna ?: "",
            close_pana = closePanna ?: "",
            points = enterBalance.toString()
        )

        Log.d("FullSangam", "Request Data: $requestData")

        RetrofitInstance.apiInterface.storeFullSangam(requestData)
            .enqueue(object : Callback<FullSangamResponse?> {
                override fun onResponse(
                    p0: Call<FullSangamResponse?>,
                    p1: Response<FullSangamResponse?>
                ) {
                    Log.d("FullSangam", "API Response: ${p1.body()}")
                    if (p1.isSuccessful) {
                        Toast.makeText(
                            this@FullSangam,
                            p1.body()?.message ?: "Success",
                            Toast.LENGTH_SHORT
                        ).show()
                        currentBalance = userBalance - enterBalance
                        // After successful bid update user account balance
                        updateUserBalance()
                        val navigate = Intent(this@FullSangam, MainActivity::class.java)
                        navigate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(navigate)
                        finish()
                    } else {
                        Log.d("FullSangam", "API Response failed: ${p1.errorBody()}")
                        Toast.makeText(
                            this@FullSangam,
                            "Failed to store Full Sangam",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(p0: Call<FullSangamResponse?>, p1: Throwable) {
                    Log.e("FullSangam", "API Request Failed: ${p1.message}")
                    Toast.makeText(this@FullSangam, "Error: ${p1.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun updateUserBalance() {
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
                        Toast.makeText(this@FullSangam, responseBody, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this@FullSangam,
                            "Failed to update balance",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@FullSangam, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getPanna() {
        val gameOpenPanna = binding.openPanna.text.toString().trim()
        val gameClosePanna = binding.closePanna.text.toString().trim()

        if (gameOpenPanna.length != 3 || gameClosePanna.length != 3) {
            Log.d(
                "FullSangam",
                "Panna validation failed: OpenPanna: $gameOpenPanna, ClosePanna: $gameClosePanna"
            )
            Toast.makeText(this, "Panna should be exactly 3 digits (111-999)", Toast.LENGTH_SHORT)
                .show()
            return
        }

        try {
            val digit = gameOpenPanna.toInt()
            val digit2 = gameClosePanna.toInt()

            if (digit in 111..999) {
                openPanna = gameOpenPanna
            } else {
                Log.d("FullSangam", "Open Panna is out of range")
                Toast.makeText(this, "Open Panna is out of range (111-999)", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            if (digit2 in 111..999) {
                closePanna = gameClosePanna
            } else {
                Log.d("FullSangam", "Close Panna is out of range")
                Toast.makeText(this, "Close Panna is out of range (111-999)", Toast.LENGTH_SHORT)
                    .show()
                return
            }
        } catch (e: NumberFormatException) {
            Log.e("FullSangam", "NumberFormatException: ${e.message}")
            Toast.makeText(this, "Invalid input, must be a number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPoint() {
        val gamePoint = binding.points.text.toString().trim()
        Log.d("FullSangam", "Points Entered: $gamePoint")

        if (gamePoint.isNotEmpty()) {
            val point = gamePoint.toIntOrNull()

            if (point != null && point >= 10 && point <= userBalance) {
                enterBalance = point
            } else {
                Log.d("FullSangam", "Invalid point value or point exceeds user balance")
            }
        } else {
            Toast.makeText(this, "Minimum Bid Is 10 Points", Toast.LENGTH_SHORT).show()
        }
    }
}
