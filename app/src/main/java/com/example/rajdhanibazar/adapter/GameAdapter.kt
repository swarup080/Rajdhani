package com.example.rajdhanibazar.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.rajdhanibazar.activity.GamePlayActivity
import com.example.rajdhanibazar.data.Game
import com.example.rajdhanibazar.databinding.GamesUiBinding
import com.example.rajdhanibazar.utlis.DoublePanna
import com.example.rajdhanibazar.utlis.FullSangam
import com.example.rajdhanibazar.utlis.HalfSangam
import com.example.rajdhanibazar.utlis.JodiDigit
import com.example.rajdhanibazar.utlis.SingleDigit
import com.example.rajdhanibazar.utlis.SinglePanna
import com.example.rajdhanibazar.utlis.TriplePanna

class GameAdapter(private val gameInfo: List<Game>, private val context: Context) : BaseAdapter() {

    override fun getCount(): Int {
        return gameInfo.size
    }

    override fun getItem(position: Int): Any {
        return gameInfo[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: GamesUiBinding
        val view: View

        if (convertView == null) {
            binding = GamesUiBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            binding.also { view.tag = it }
        } else {
            binding = convertView.tag as GamesUiBinding
            view = convertView
        }

        val game = gameInfo[position]
        binding.gameImage.setImageResource(game.gameImage)
        binding.gameName.text = game.gameName
        view.setOnClickListener {
            if (game.gameName == "Single Digit") {
                val intent = Intent(context, SingleDigit::class.java)
                intent.putExtra("gameName", game.gameName)
                context.startActivity(intent)
            } else if (game.gameName == "Jodi Digit") {
                val intent = Intent(context, JodiDigit::class.java)
                intent.putExtra("gameName", game.gameName)
                context.startActivity(intent)
            } else if (game.gameName == "Single Panna") {
                val intent = Intent(context, SinglePanna::class.java)
                intent.putExtra("gameName", game.gameName)
                context.startActivity(intent)
            } else if (game.gameName == "Double Panna") {
                val intent = Intent(context, DoublePanna::class.java)
                intent.putExtra("gameName", game.gameName)
                context.startActivity(intent)
            } else if (game.gameName == "Triple Panna") {
                val intent = Intent(context, TriplePanna::class.java)
                intent.putExtra("gameName", game.gameName)
                context.startActivity(intent)
            } else if (game.gameName == "Half Sangam") {
                val intent = Intent(context, HalfSangam::class.java)
                intent.putExtra("gameName", game.gameName)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, FullSangam::class.java)
                intent.putExtra("gameName", game.gameName)
                context.startActivity(intent)
            }

        }
        return view
    }
}