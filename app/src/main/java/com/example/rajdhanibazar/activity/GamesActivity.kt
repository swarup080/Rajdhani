package com.example.rajdhanibazar.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.adapter.GameAdapter

import com.example.rajdhanibazar.data.Game
import com.example.rajdhanibazar.databinding.ActivityGamesBinding

class GamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesBinding
    private lateinit var gameAdapter: GameAdapter
    private lateinit var gameList: List<Game>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //This data is comes from market adapter
        val gameName: String = intent.getStringExtra("market").toString()

        val sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("marketName", gameName)
        editor.apply()
        binding.marketNameGet.text = gameName
        //
        gameList = listOf(
            Game(R.drawable.game_rates,"Single Digit"),
            Game(R.drawable.game_rates,"Jodi Digit"),
            Game(R.drawable.game_rates,"Single Panna"),
            Game(R.drawable.game_rates,"Double Panna"),
            Game(R.drawable.game_rates,"Triple Panna"),
            Game(R.drawable.game_rates,"Half Sangam"),
            Game(R.drawable.game_rates,"Full Sangam"),
        )
        gameAdapter = GameAdapter(gameList,this)
        binding.categoryList.adapter = gameAdapter
    }
}