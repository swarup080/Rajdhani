package com.example.rajdhanibazar.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.databinding.ActivityStarlineInfoBinding
import com.example.rajdhanibazar.utlis.GaliJodiDigit
import com.example.rajdhanibazar.utlis.GaliLeftDigit
import com.example.rajdhanibazar.utlis.GaliRightDigit
import com.example.rajdhanibazar.utlis.StarlineDoublePanna
import com.example.rajdhanibazar.utlis.StarlineSingleDigit
import com.example.rajdhanibazar.utlis.StarlineSinglePanna
import com.example.rajdhanibazar.utlis.StarlineTriplePanna

class StarlineInfo : AppCompatActivity() {
    private lateinit var binding: ActivityStarlineInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStarlineInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setOnClick()
    }

    private fun setOnClick() {
        binding.singleDigit.setOnClickListener {
            val intent = Intent(this, StarlineSingleDigit::class.java)
            intent.putExtra("gameName", binding.left.text.toString())
            intent.putExtra("market", binding.galiName.text.toString())
            startActivity(intent)
        }
        binding.singlePanna.setOnClickListener {
            val intent = Intent(this, StarlineSinglePanna::class.java)
            intent.putExtra("gameName", binding.right.text.toString())
            intent.putExtra("market", binding.galiName.text.toString())
            startActivity(intent)
        }
        binding.doublePanna.setOnClickListener {
            val intent = Intent(this, StarlineDoublePanna::class.java)
            intent.putExtra("gameName", binding.jodi.text.toString())
            intent.putExtra("market", binding.galiName.text.toString())
            startActivity(intent)
        }
        binding.triplePanna.setOnClickListener{
            val intent = Intent(this, StarlineTriplePanna::class.java)
            intent.putExtra("gameName", binding.triple.text.toString())
            intent.putExtra("market", binding.galiName.text.toString())
            startActivity(intent)
        }
    }
}
