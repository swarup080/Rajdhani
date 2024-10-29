package com.example.rajdhanibazar.adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.activity.GamesActivity
import com.example.rajdhanibazar.data.Market
import java.util.Calendar

class MarketAdapter(private val context: Context, private val marketList: List<Market>) :
    RecyclerView.Adapter<MarketAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.market_status_ui, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = marketList[position]
        holder.marketImage.setImageResource(R.drawable.rajdhani)
        holder.marketName.text = market.marketName
        holder.openTime.text = market.openTime
        holder.closeTime.text = market.closeTime
        holder.marketResult.text = if (market.result.isNullOrEmpty()) "******" else market.result

        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Parse market open and close time
        val openTimeParts = market.openTime.split(":").map { it.toInt() }
        val closeTimeParts = market.closeTime.split(":").map { it.toInt() }

        // Extract hours and minutes for open and close times
        val openHour = openTimeParts[0]
        val openMinute = openTimeParts[1]
        val closeHour = closeTimeParts[0]
        val closeMinute = closeTimeParts[1]

        // Get current time in minutes since midnight
        val currentTime = currentHour * 60 + currentMinute
        val openTime = openHour * 60 + openMinute
        val closeTime = closeHour * 60 + closeMinute

        // Logic for keeping the market open all the time except after closing
        val midnightTime = 0 // 12 AM in minutes
        val isMarketClosed = currentTime >= closeTime // Market closes after its closing time

        // Logic for reopening at 12 AM the next day
        val isAfterMidnight = currentTime >= midnightTime && currentHour == 0

        // Market remains open until it hits closing time, then reopens at midnight the next day
        val isOpen = if (isMarketClosed) {
            // After market close, market remains closed until 12 AM the next day
            isAfterMidnight
        } else {
            // Market is always open before the close time
            true
        }

        // Update the UI based on whether the market is open or closed
        val (backgroundResId, iconResId, clickListener) = if (isOpen) {
            Triple(R.drawable.market_open_bg, R.drawable.green_arrow, View.OnClickListener {
                val intent = Intent(context, GamesActivity::class.java)
                intent.putExtra("market", market.marketName)
                context.startActivity(intent)
            })
        } else {
            Triple(R.drawable.market_close_bg, R.drawable.red__arrow, View.OnClickListener {
                Toast.makeText(context, "Market is closed", Toast.LENGTH_SHORT).show()
            })
        }

        // Set background, icon, and market status text
        holder.marketStatusBackground?.setBackgroundResource(backgroundResId)
        holder.marketStatusIcon.setImageResource(iconResId)
        holder.marketStatus.text = if (isOpen) "Open" else "Closed"
        holder.itemView.setOnClickListener(clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val marketImage: ImageView = itemView.findViewById(R.id.marketImage)
        val marketName: TextView = itemView.findViewById(R.id.marketName)
        val openTime: TextView = itemView.findViewById(R.id.openTime)
        val closeTime: TextView = itemView.findViewById(R.id.closeTime)
        val marketResult: TextView = itemView.findViewById(R.id.marketResult)
        val marketStatusBackground: View? = itemView.findViewById(R.id.marketStatusBackground)
        val marketStatusIcon: ImageView = itemView.findViewById(R.id.marketStatusIcon)
        val marketStatus: TextView = itemView.findViewById(R.id.marketStatus)
    }
}
