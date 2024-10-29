package com.example.rajdhanibazar.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.databinding.ActivityAddFundBinding

class AddFundActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFundBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeSharedPreferences()
        setupContactButton()
        // Setting up radio button tint change on selection
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            handleRadioButtonSelection(checkedId)
        }
        binding.save.setOnClickListener {
            val amountText = binding.amount.text.toString()
            if (amountText.isNotEmpty()) {
                val amount = amountText.toDoubleOrNull()
                if (amount != null && amount > 0) {
                    val selectedPaymentMethod = getSelectedPaymentMethod()
                    if (selectedPaymentMethod != null) {
                        Log.d("AddFundActivity", "Selected Payment Method: $selectedPaymentMethod")
                        openPaymentApp(selectedPaymentMethod, amountText)
                    } else {
                        Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter a valid positive amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to initialize SharedPreferences
    private fun initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
        val userBalance = sharedPreferences.getInt("userBalance", 0)
        binding.textViewAccountBalance.text = "Your Account Balance Is: $${String.format("%,d", userBalance)}"
    }

    // Function to get the selected payment method
    private fun getSelectedPaymentMethod(): String? {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        return when (selectedId) {
            R.id.phonepay -> "PhonePay"
            R.id.gpay -> "GooglePay"
            R.id.paytm -> "Paytm"
            else -> null
        }
    }

    // Function to open the appropriate payment app
    private fun openPaymentApp(paymentMethod: String, amount: String) {
        val upiId = "meenachetram8798@okhdfcbank" // Replace with actual UPI ID
        val packageName = when (paymentMethod) {
            "PhonePay" -> "com.phonepe.app"
            "GooglePay" -> "com.google.android.apps.nbu.paisa.user"
            "Paytm" -> "net.one97.paytm"
            else -> null
        }

        Log.d("AddFundActivity", "Trying to open $paymentMethod with package: $packageName")

        if (packageName != null && isAppInstalled(packageName)) {
            Log.d("AddFundActivity", "Opening $paymentMethod for amount: $amount")
            openUPIApp(upiId, amount, packageName)
        } else {
            Log.e("AddFundActivity", "$packageName app is not installed or invalid package")
            Toast.makeText(this, "$paymentMethod app is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to open UPI app
    private fun openUPIApp(upiId: String, amount: String, packageName: String) {
        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(packageName)

        // Check if the app is installed
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Log.e("AddFundActivity", "Failed to open $packageName")
            Toast.makeText(this, "$packageName app is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to check if the app is installed
    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    // Function to set up the WhatsApp contact button
    private fun setupContactButton() {
        binding.whatsappContact.setOnClickListener {
            val url = "https://wa.me/+916367223825"
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // Set the package to open WhatsApp if installed
                intent.setPackage("com.whatsapp")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "WhatsApp is not installed on your phone", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Function to handle radio button selection and set tint
    private fun handleRadioButtonSelection(checkedId: Int) {
        val radioButton: RadioButton = findViewById(checkedId)

        // Set default tint for all radio buttons
        val defaultTint = ColorStateList.valueOf(resources.getColor(R.color.black, theme))

        // Reset tint for all radio buttons
        binding.phonepay.buttonTintList = defaultTint
        binding.gpay.buttonTintList = defaultTint
        binding.paytm.buttonTintList = defaultTint

        // Set tint for the selected radio button
        radioButton.buttonTintList = ColorStateList.valueOf(resources.getColor(R.color.white, theme))
    }
}
