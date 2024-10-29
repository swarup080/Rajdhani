package com.example.rajdhanibazar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.data.GameResult
import com.example.rajdhanibazar.databinding.ResultUiBinding

class ResultAdapter(
    private val context: Context,
    private val resultList: List<GameResult>
) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ResultUiBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = resultList[position]
        holder.bind(result)

    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    class ViewHolder(private val binding: ResultUiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: GameResult) {
            binding.tvMarket.text = "Market: ${result.market}"
            binding.tvDate.text = "Date: ${result.date}"
            binding.tvSession.text = "Session: ${if (result.session == "1") "Open" else "Close"}"
            binding.tvOpenPana.text = "Open Pana: ${result.openPana}"
            binding.tvOpenResult.text = "Open Result: ${result.openResult}"
            binding.tvClosePana.text = "Close Pana: ${result.closePana}"
            binding.tvCloseResult.text = "Close Result: ${result.closeResult}"
        }
    }
}