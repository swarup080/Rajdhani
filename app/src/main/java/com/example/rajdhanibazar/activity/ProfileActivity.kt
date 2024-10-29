package com.example.rajdhanibazar.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var sharedPreferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        contact()
        initializeSharedPreferences()
    }
    private fun contact(){
        //open whatsapp chat on a particular number
        binding.whatsappContact.setOnClickListener{
            val url = "https://wa.me/+916367223825"
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.setPackage("com.whatsapp")
                startActivity(intent)
            }catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(this,"Whatsapp is not installed in your phone", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Function to initialize SharedPreferences
    private fun initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", null) ?: "N/A"
        val email = sharedPreferences.getString("userEmail", null) ?: "N/A"
        val phoneNumber = sharedPreferences.getString("phoneNumber", null) ?: "N/A"
        val userBalance = sharedPreferences.getInt("userBalance", 0)

        binding.textViewName.text = userName
        binding.textViewEmail.text = email
        binding.textViewPhoneNumber.text = phoneNumber
        binding.textViewAccountBalance.text = "Point:  ${userBalance}"

    }
}