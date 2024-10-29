package com.example.rajdhanibazar.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.activity.StarlineInfo
import com.example.rajdhanibazar.data.StarlineGame
import com.example.rajdhanibazar.databinding.StarlineUiBinding

class StarlineAdapter(private val context: Context, private val starlineList: List<StarlineGame>) :
    RecyclerView.Adapter<StarlineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StarlineUiBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val starline = starlineList[position]
        holder.bind(starline)
    }

    override fun getItemCount(): Int {
        return starlineList.size
    }

    inner class ViewHolder(private val binding: StarlineUiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(starline: StarlineGame) {
            binding.time.text = starline.time
            binding.result.text = starline.result

            // Check the status (1 for open, 0 for closed) and bind accordingly
            if (starline.status == "1") {
                binding.marketStatus.text = "Open"
                binding.marketStatusBackground.setBackgroundResource(R.drawable.market_open_bg)
                binding.icon.setImageResource(R.drawable.green_arrow) // Green arrow for open status
            } else {
                binding.marketStatus.text = "Closed"
                binding.marketStatusBackground.setBackgroundResource(R.drawable.market_close_bg)
                binding.icon.setImageResource(R.drawable.red__arrow) // Red arrow for closed status
            }

            // Click listener for the root layout
            binding.root.setOnClickListener {
                if (starline.status == "0") {
                    Toast.makeText(context, "Market is closed", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(context, StarlineInfo::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }
}
