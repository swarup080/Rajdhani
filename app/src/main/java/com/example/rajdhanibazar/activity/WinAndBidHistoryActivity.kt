package com.example.rajdhanibazar.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.adapter.ResultAdapter
import com.example.rajdhanibazar.data.GameResult
import com.example.rajdhanibazar.databinding.ActivityWinAndBidHistoryBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WinAndBidHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWinAndBidHistoryBinding
    private lateinit var resultAdapter: ResultAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinAndBidHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        showResult()
    }

    private fun showResult() {
        binding.winHistoryRecycler.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.apiInterface.getResults().enqueue(object : Callback<List<GameResult>> {
            override fun onResponse(call: Call<List<GameResult>>, response: Response<List<GameResult>>) {
                if (response.isSuccessful) {
                    val results = response.body()
                    if (!results.isNullOrEmpty()) {
                        Log.d("WinAndBidHistoryActivity", "Results: $results")
                        resultAdapter = ResultAdapter(this@WinAndBidHistoryActivity, results)
                        binding.winHistoryRecycler.adapter = resultAdapter
                    } else {
                        Log.e("WinAndBidHistoryActivity", "No results or empty response")
                    }
                } else {
                    Log.e("WinAndBidHistoryActivity", "Response error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GameResult>>, t: Throwable) {
                Log.e("WinAndBidHistoryActivity", "API call failure: ${t.message}")
            }
        })
    }


}