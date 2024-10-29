package com.example.rajdhanibazar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rajdhanibazar.data.GameRate
import com.example.rajdhanibazar.databinding.GameRateUiBinding
class GameRateAdapter(private val context: Context, private val gameRateList: List<GameRate>):
    RecyclerView.Adapter<GameRateAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameRateAdapter.ViewHolder {
        val binding = GameRateUiBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameRate = gameRateList[position]
        Log.d("GameRateAdapter", "Binding data: ${gameRate.name} - ${gameRate.rate}")
        holder.bind(gameRate)
    }

    override fun getItemCount(): Int {
        return gameRateList.size
    }

    class ViewHolder(private val binding: GameRateUiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gameRate: GameRate) {
            binding.name.text = gameRate.name ?: "No Name"
            binding.rate.text = gameRate.rate ?: "No Rate"
        }
    }

}