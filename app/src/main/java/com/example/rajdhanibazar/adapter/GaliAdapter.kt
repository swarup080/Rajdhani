package com.example.rajdhanibazar.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.activity.GaliInfo
import com.example.rajdhanibazar.data.Gali
import com.example.rajdhanibazar.databinding.GaliUiBinding

class GaliAdapter(private val galiList: List<Gali>, private val context: Context) :
    RecyclerView.Adapter<GaliAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GaliUiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gali = galiList[position]
        holder.bind(gali)
    }

    override fun getItemCount(): Int = galiList.size

    class ViewHolder(private val binding: GaliUiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gali: Gali) {
            binding.name.text = gali.name
            binding.marketStatus.text = if (gali.marketStatus == "1") "Open" else "Close"
            binding.result.text = gali.result
            binding.time.text = gali.time

            if (gali.marketStatus == "0") {
                binding.marketStatusBackground.setBackgroundResource(R.drawable.market_close_bg)
                binding.icon.setImageResource(R.drawable.red__arrow)
                binding.root.setOnClickListener {
                    Toast.makeText(binding.root.context, "Market Closed", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.marketStatusBackground.setBackgroundResource(R.drawable.market_open_bg)
                binding.icon.setImageResource(R.drawable.green_arrow)
                // Set click listener to navigate to another activity
                binding.root.setOnClickListener {
                    val context = binding.root.context
                    val intent = Intent(context, GaliInfo::class.java)
                    // Pass necessary data if needed
                    intent.putExtra("galiId", gali.id)
                    intent.putExtra("galiName", gali.name)
                    context.startActivity(intent)
                }
            }
        }
    }
}