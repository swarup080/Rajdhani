package com.example.rajdhanibazar.utlis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.activity.MainActivity
import com.example.rajdhanibazar.data.AddBid
import com.example.rajdhanibazar.data.Balance
import com.example.rajdhanibazar.data.BidResponse
import com.example.rajdhanibazar.databinding.ActivitySinglePannaBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SinglePanna : AppCompatActivity() {
    private lateinit var binding: ActivitySinglePannaBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName: String
    private lateinit var email: String
    private var phoneNumber: String? = null

    private var gameName: String? = null
    private var marketName: String? = null

    private var gameDate: String? = null
    private var gameSession: String? = null
    private var storedDigit: String? = null
    private var gamePointAmount: String? = null


    //  Manipulate account balance
    private var enterBalance: Int = 0
    private var userBalance: Int = 0
    private var currentBalance: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePannaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeSharedPreferences()
        retrieveIntentData()
        setupDate()
        setupRadioGroupListener()
        setupAddBidButtonListener()
    }

    // Function to initialize SharedPreferences
    private fun initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
        userName = sharedPreferences.getString("userName", null).toString()
        email = sharedPreferences.getString("userEmail", null).toString()
        phoneNumber = sharedPreferences.getString("phoneNumber", null)
        marketName = sharedPreferences.getString("marketName", null)

        userBalance = sharedPreferences.getInt("userBalance", 0)
    }

    // Function to retrieve data passed through the intent
    private fun retrieveIntentData() {
        gameName = intent.getStringExtra("gameName")
        binding.gamePlayName.text = gameName
    }

    // Function to set the current date
    private fun setupDate() {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val currentDate: String = dateFormat.format(calendar.time)
        binding.gameDate.setText(currentDate)
        gameDate = binding.gameDate.text.toString()
    }

    // Function to handle RadioGroup selection
    private fun setupRadioGroupListener() {
        var selectedRadioButton: RadioButton? = null

        binding.radioGrp.setOnCheckedChangeListener { _, checkedId ->
            selectedRadioButton = binding.radioGrp.findViewById(checkedId)

            if (checkedId != -1) {
                gameSession = selectedRadioButton?.text?.toString() ?: ""
            }
        }
    }

    // Function to handle Add Bid button click
    private fun setupAddBidButtonListener() {
        binding.addBid.setOnClickListener {
            getPanna()
            getPoint()
            gamePointAmount = enterBalance.toString()

            when {
                storedDigit == null -> {
                    Toast.makeText(this, "Please enter a valid panna", Toast.LENGTH_SHORT).show()
                }

                userBalance <= enterBalance -> {  // Check if user balance is less than the bid amount
                    Toast.makeText(
                        this,
                        "Insufficient balance",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                enterBalance == 0 -> {
                    Toast.makeText(this, "Please enter a valid point", Toast.LENGTH_SHORT).show()
                }

                gameSession == null -> {
                    Toast.makeText(this, "Please enter a valid session", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    // Adding Bid to db
                    bidHistory(
                        marketName,
                        gameName,
                        storedDigit,
                        gamePointAmount,
                        gameDate,
                        gameSession,
                        userName,
                        phoneNumber,
                        email
                    )
                    Toast.makeText(
                        this,
                        "Bid Added: Session = $gameSession, Digit = $storedDigit, Points = $gamePointAmount",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // Function to validate and retrieve the digit
    private fun getPanna() {
        val gameOpenPanna = binding.openPanna.text.toString()

        if (gameOpenPanna.length > 4) {
            Toast.makeText(this, "Panna should be 111-999", Toast.LENGTH_SHORT).show()
        } else {
            if (gameOpenPanna.isNotEmpty() && gameOpenPanna[0].isDigit()) {
                val digit = gameOpenPanna.toInt()
                if (digit in 111..999) {
                    storedDigit = gameOpenPanna
                }
            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to validate and retrieve the points
    private fun getPoint() {
        val gamePoint = binding.points.text.toString().trim()

        if (gamePoint.isNotEmpty()) {
            val point = gamePoint.toIntOrNull()

            if (point != null && point >= 10 && point <= userBalance) {
                enterBalance = point
            }
        } else {
            Toast.makeText(this, "Minimum Bid Is 10 Points", Toast.LENGTH_SHORT).show()
        }
    }

    // Send this data to bid history api
    private fun bidHistory(
        marketName: String?,
        gameName: String?,
        storedDigit: String?,
        gamePointAmount: String?,
        gameDate: String?,
        gameSession: String?,
        userName: String?,
        phoneNumber: String?,
        email: String?
    ) {
        val addBid = AddBid(
            marketName,
            gameName,
            storedDigit,
            gamePointAmount,
            gameDate,
            gameSession,
            userName,
            phoneNumber,
            email
        )
        // Check if the user's balance is sufficient to place the bid
        if (userBalance >= enterBalance) {
            RetrofitInstance.apiInterface.postBid(addBid).enqueue(object : Callback<BidResponse?> {
                override fun onResponse(p0: Call<BidResponse?>, p1: Response<BidResponse?>) {
                    if (p1.isSuccessful) {
                        val bidResponse = p1.body()
                        currentBalance = userBalance - enterBalance
                        // After successful bid update user account balance
                        updateUserBalance()
                        val navigate = Intent(this@SinglePanna, MainActivity::class.java)
                        navigate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(navigate)
                        finish()
                        Toast.makeText(this@SinglePanna, bidResponse?.message, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@SinglePanna, "Failed to create bid", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(p0: Call<BidResponse?>, p1: Throwable) {
                    Toast.makeText(this@SinglePanna, p1.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Show a message if the balance is insufficient
            Toast.makeText(this@SinglePanna, "Insufficient balance", Toast.LENGTH_SHORT).show()
        }
    }

    // update user balance
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
                        Toast.makeText(this@SinglePanna, responseBody, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this@SinglePanna,
                            "Failed to update balance",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@SinglePanna, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}