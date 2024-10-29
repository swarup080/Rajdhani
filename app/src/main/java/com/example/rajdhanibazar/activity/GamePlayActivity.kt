package com.example.rajdhanibazar.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.databinding.ActivityGamePlayBinding

class GamePlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamePlayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //This data is comes from game adapter
        val gameName = intent.getStringExtra("gameName")
        binding.gamePlayName.text = gameName
    }
}