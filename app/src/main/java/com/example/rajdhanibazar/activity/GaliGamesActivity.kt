package com.example.rajdhanibazar.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rajdhanibazar.adapter.GaliAdapter
import com.example.rajdhanibazar.data.Gali
import com.example.rajdhanibazar.data.GaliResponse
import com.example.rajdhanibazar.databinding.ActivityGaliGamesBinding
import com.example.rajdhanibazar.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GaliGamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGaliGamesBinding
    private lateinit var galiAdapter: GaliAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGaliGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        galiInfo()
    }

    private fun galiInfo() {
        // Initialize the RecyclerView
        binding.galiRecycler.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.apiInterface.getGaliGames().enqueue(object : retrofit2.Callback<GaliResponse> {
            override fun onResponse(call: Call<GaliResponse>, response: Response<GaliResponse>) {
                if (response.isSuccessful) {
                    val galiResponse = response.body()
                    if (galiResponse != null && galiResponse.success) {
                        // Sort the list by time before passing it to the adapter
                        val galiList = galiResponse.data.sortedBy { it.time }
                        galiAdapter = GaliAdapter(galiList, this@GaliGamesActivity)
                        binding.galiRecycler.adapter = galiAdapter
                    } else {
                        Log.d("GaliGamesActivity", "GaliResponse unsuccessful or data is null")
                    }
                } else {
                    Log.d("GaliGamesActivity", "Response was not successful")
                }
            }

            override fun onFailure(call: Call<GaliResponse>, t: Throwable) {
                Log.e("GaliGamesActivity", "Failed to fetch Gali data", t)
            }
        })
    }
}