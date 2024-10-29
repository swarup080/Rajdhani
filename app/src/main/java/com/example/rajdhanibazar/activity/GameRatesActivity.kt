package com.example.rajdhanibazar.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.adapter.GameRateAdapter
import com.example.rajdhanibazar.data.GameRate
import com.example.rajdhanibazar.databinding.ActivityGameRatesBinding
import com.example.rajdhanibazar.databinding.GameRateUiBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameRatesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameRatesBinding
    private lateinit var gameRateAdapter: GameRateAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameRatesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        gameRate()
    }

    private fun gameRate() {
        binding.gameRateRecycler.layoutManager = LinearLayoutManager(this)
        RetrofitInstance.apiInterface.getGameRate().enqueue(object : Callback<List<GameRate>> {
            override fun onResponse(call: Call<List<GameRate>>, response: Response<List<GameRate>>) {
                if (response.isSuccessful) {
                    val gameRateList = response.body() ?: emptyList()
                    Log.d("GameRatesActivity", "Results: $gameRateList")
                    gameRateAdapter = GameRateAdapter(this@GameRatesActivity, gameRateList)
                    binding.gameRateRecycler.adapter = gameRateAdapter
                } else {
                    // Handle non-successful response
                    Log.e("GameRatesActivity", "Response error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GameRate>>, t: Throwable) {
                // Handle failure
                Log.e("GameRatesActivity", "API call failure: ${t.message}")
            }
        })
    }

}