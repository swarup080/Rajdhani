package com.example.rajdhanibazar.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rajdhanibazar.adapter.StarlineAdapter
import com.example.rajdhanibazar.data.StarlineGamesResponse
import com.example.rajdhanibazar.databinding.ActivityStarLineGamesBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StarLineGamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStarLineGamesBinding
    private lateinit var starlineAdapter: StarlineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarLineGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        starlineInfo()
    }

    private fun starlineInfo() {
        binding.starlineRecycler.layoutManager = LinearLayoutManager(this)


        RetrofitInstance.apiInterface.getStarlineGames().enqueue(object : Callback<StarlineGamesResponse> {
            override fun onResponse(
                call: Call<StarlineGamesResponse>,
                response: Response<StarlineGamesResponse>
            ) {
                if (response.isSuccessful) {
                    val starlineResponse = response.body()

                    if (starlineResponse != null && starlineResponse.success) {
                        val starlineList = starlineResponse.data.sortedBy { it.time }
                        starlineAdapter = StarlineAdapter(this@StarLineGamesActivity, starlineList)
                        binding.starlineRecycler.adapter = starlineAdapter
                    } else {
                        Log.d("StarLineGamesActivity", "GaliResponse unsuccessful or data is null")
                    }
                } else {
                    Log.e("API Error", "Failed to fetch data")
                }
            }

            override fun onFailure(call: Call<StarlineGamesResponse>, t: Throwable) {
                Log.e("API Failure", "Error: ${t.message}")
            }
        })
    }
}
