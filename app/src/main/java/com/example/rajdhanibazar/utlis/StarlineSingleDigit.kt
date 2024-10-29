package com.example.rajdhanibazar.utlis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.activity.MainActivity
import com.example.rajdhanibazar.data.AddBid
import com.example.rajdhanibazar.data.Balance
import com.example.rajdhanibazar.data.BidResponse
import com.example.rajdhanibazar.databinding.ActivitySingleDigitBinding
import com.example.rajdhanibazar.databinding.ActivityStarlineSingleDigitBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class StarlineSingleDigit : AppCompatActivity() {
    private lateinit var binding: ActivityStarlineSingleDigitBinding
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
        enableEdgeToEdge()
        binding = ActivityStarlineSingleDigitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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


        userBalance = sharedPreferences.getInt("userBalance", 0)
    }

    // Function to retrieve data passed through the intent
    private fun retrieveIntentData() {
        gameName = intent.getStringExtra("gameName")
        marketName = intent.getStringExtra("market")
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
            getDigit()
            getPoint()
            gamePointAmount = enterBalance.toString()

            when {
                storedDigit == null -> {
                    Toast.makeText(this, "Please enter a valid digit", Toast.LENGTH_SHORT).show()
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
    private fun getDigit() {
        val gameOpenDigit = binding.openDigit.text.toString()

        if (gameOpenDigit.length > 1) {
            Toast.makeText(this, "Digit should be 0-9", Toast.LENGTH_SHORT).show()
        } else {
            if (gameOpenDigit.isNotEmpty() && gameOpenDigit[0].isDigit()) {
                val digit = gameOpenDigit.toInt()
                if (digit in 0..9) {
                    storedDigit = gameOpenDigit
                } else {
                    Toast.makeText(this, "Invalid digit", Toast.LENGTH_SHORT).show()
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
            } else {
//                Toast.makeText(
//                    this,
//                    "Please enter a valid point amount within your balance",
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
            }
        } else {
            Toast.makeText(this, "Minimum Bid Is 10 Points", Toast.LENGTH_SHORT).show()
        }
    }

    // Send this data to bid history API
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
            // Make the API call to post the bid
            RetrofitInstance.apiInterface.postBid(addBid).enqueue(object : Callback<BidResponse?> {
                override fun onResponse(
                    call: Call<BidResponse?>,
                    response: Response<BidResponse?>
                ) {
                    if (response.isSuccessful) {
                        val bidResponse = response.body()

                        // Update the current balance after a successful bid
                        currentBalance = userBalance - enterBalance
                        updateUserBalance()

                        // Navigate back to MainActivity
                        val navigate = Intent(this@StarlineSingleDigit, MainActivity::class.java)
                        navigate.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                        startActivity(navigate)
                        finish()

                        // Show a success message
                        Toast.makeText(this@StarlineSingleDigit, bidResponse?.message, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // Handle failure in bid creation
                        Toast.makeText(this@StarlineSingleDigit, "Failed to create bid", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<BidResponse?>, t: Throwable) {
                    // Handle network or other failures
                    Toast.makeText(this@StarlineSingleDigit, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Show a message if the balance is insufficient
            Toast.makeText(this@StarlineSingleDigit, "Insufficient balance", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@StarlineSingleDigit, responseBody, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this@StarlineSingleDigit,
                            "Failed to update balance",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@StarlineSingleDigit, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}