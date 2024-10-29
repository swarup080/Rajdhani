package com.example.rajdhanibazar.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.data.Balance
import com.example.rajdhanibazar.data.Withdraw
import com.example.rajdhanibazar.data.WithdrawResponse
import com.example.rajdhanibazar.databinding.ActivityWithdrawFundBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class WithdrawFundActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWithdrawFundBinding
    private var userBalance: Int = 0

    private lateinit var name: String
    private lateinit var number: String
    private lateinit var balance: String
    private lateinit var amount: String
    private lateinit var date: String

    var phonePe: String? = null
    var googlePay: String? = null
    var paytm: String? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawFundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeSharedPreferences()
        changeBg()
        binding.save.setOnClickListener {
            if (validateInputs()) {
                postWithdrawReq()
            }
        }
    }

    private fun initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
        userBalance = sharedPreferences.getInt("userBalance", 0)
        binding.textViewAccountBalance.text = "Account Balance (Point) Is: $userBalance"
        name = sharedPreferences.getString("userName", "N/A") ?: "N/A"

        // Initialize number from input

        balance = userBalance.toString()

        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        date = dateFormat.format(calendar.time)
    }

    private fun changeBg() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                val radioButton = group.getChildAt(i) as RadioButton
                if (radioButton.id == checkedId) {
                    radioButton.buttonTintList = ColorStateList.valueOf(resources.getColor( R.color.white))
                    when (radioButton.text.toString()) {
                        "Phone Pay" -> {
                            phonePe = radioButton.text.toString()
                            paytm = null
                            googlePay = null
                        }
                        "Google Pay" -> {
                            phonePe = null
                            paytm = null
                            googlePay = radioButton.text.toString()
                        }
                        else -> {
                            phonePe = null
                            paytm = radioButton.text.toString()
                            googlePay = null
                        }
                    }
                } else {
                    radioButton.buttonTintList = ColorStateList.valueOf(resources.getColor( R.color.black))
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        amount = binding.amount.text.toString()
        number = binding.mobileNumber.text.toString()
        if (amount.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return false
        }
        val enteredAmount = amount.toIntOrNull()
        if (enteredAmount == null || enteredAmount < 1000) {
            Toast.makeText(this, "Minimum withdrawal amount is 1000", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phonePe.isNullOrEmpty() && googlePay.isNullOrEmpty() && paytm.isNullOrEmpty()) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            return false
        }
        if (number.isEmpty()) {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun postWithdrawReq() {
        val enteredAmount = amount.toIntOrNull() ?: return

        if (enteredAmount < 1000) {
            Toast.makeText(this, "Minimum withdrawal amount is 1000", Toast.LENGTH_SHORT).show()
            return
        }

        if (userBalance < enteredAmount) {
            Toast.makeText(this, "Insufficient account balance", Toast.LENGTH_SHORT).show()
            return
        }

        val withdrawRequest = Withdraw(
            name = name,
            number = number,
            balance = balance,
            amount = amount,
            date = date,
            phonepe = phonePe ?: "",
            google_pay = googlePay ?: "",
            paytm = paytm ?: ""
        )

        val withdrawCall = RetrofitInstance.apiInterface.postWithdrawRequest(withdrawRequest)
        withdrawCall.enqueue(object : Callback<WithdrawResponse> {
            override fun onResponse(call: Call<WithdrawResponse>, response: Response<WithdrawResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { withdrawResponse ->
                        Toast.makeText(this@WithdrawFundActivity, withdrawResponse.message, Toast.LENGTH_SHORT).show()
                        Log.d("WithdrawFundActivity", "Withdraw Request Submitted: ${withdrawResponse.withdrawal_request}")

                        val newBalance = userBalance - enteredAmount
                        updateUserBalance(newBalance)

                    }
                } else {
                    Toast.makeText(this@WithdrawFundActivity, "Failed to submit withdraw request", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WithdrawResponse>, t: Throwable) {
                Toast.makeText(this@WithdrawFundActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUserBalance(newBalance: Int) {
        // Retrieve the phone number from SharedPreferences or other sources
        val phone = sharedPreferences.getString("phoneNumber", "N/A") ?: "N/A"
        val balanceUpdate = Balance(newBalance)

        RetrofitInstance.apiInterface.updateBalance(phone, balanceUpdate)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string()
                        Toast.makeText(this@WithdrawFundActivity, "Balance updated: $responseBody", Toast.LENGTH_SHORT).show()

                        // Update local SharedPreferences to reflect the new balance
                        sharedPreferences.edit().putInt("userBalance", newBalance).apply()

                        // Navigate back to MainActivity after balance update
                        val navigate = Intent(this@WithdrawFundActivity, MainActivity::class.java)
                        navigate.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                        startActivity(navigate)
                        finish()
                    } else {
                        Toast.makeText(this@WithdrawFundActivity, "Failed to update balance", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@WithdrawFundActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}