package com.example.rajdhanibazar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.databinding.ActivityGaliInfoBinding
import com.example.rajdhanibazar.databinding.ActivityLoginBinding
import com.example.rajdhanibazar.utlis.GaliJodiDigit
import com.example.rajdhanibazar.utlis.GaliLeftDigit
import com.example.rajdhanibazar.utlis.GaliRightDigit
import com.example.rajdhanibazar.utlis.JodiDigit

class GaliInfo : AppCompatActivity() {
    private lateinit var binding: ActivityGaliInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGaliInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.galiName.text = intent.getStringExtra("galiName")
        setOnClick()
    }

    private fun setOnClick(){
        binding.leftDigit.setOnClickListener {
            val intent = Intent(this,GaliLeftDigit::class.java)
            intent.putExtra("gameName",binding.left.text.toString())
            intent.putExtra("market",binding.galiName.text.toString())
            startActivity(intent)
        }
        binding.rightDigit.setOnClickListener {
            val intent = Intent(this,GaliRightDigit::class.java)
            intent.putExtra("gameName",binding.right.text.toString())
            intent.putExtra("market",binding.galiName.text.toString())
            startActivity(intent)
        }
        binding.jodiDigit.setOnClickListener {
            val intent = Intent(this,GaliJodiDigit::class.java)
            intent.putExtra("gameName",binding.jodi.text.toString())
            intent.putExtra("market",binding.galiName.text.toString())
            startActivity(intent)
        }
    }
}