package com.example.rajdhanibazar.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {
    private lateinit var binding:ActivityContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //User can call using this number
        binding.callFirst.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+916367223825")
            startActivity(intent)
        }
        //This is an alternate number
        binding.callSecond.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+916367223825")
            startActivity(intent)
        }
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
                Toast.makeText(this,"Whatsapp is not installed in your phone",Toast.LENGTH_SHORT).show()
            }
        }
        //telegram
        binding.telegramContact.setOnClickListener {
            val telegramUrl = "https://t.me/rajdhani_market"
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(telegramUrl)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Telegram is not installed on your phone", Toast.LENGTH_SHORT).show()
            }
        }
        //open email on a particular email address
        binding.emailContact.setOnClickListener {
            val recipient = "01hariommeena2000@gmail.com"
            val subject = "Subject Text"
            val body = "Email body text"

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$recipient")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }

            try {
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                // Show a toast message if no email clients are installed
                Toast.makeText(this, "No email clients installed on your device", Toast.LENGTH_SHORT).show()
            }
        }
        //open withdraw proof
        binding.withdrawProof.setOnClickListener {
            Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show()
        }
    }
}