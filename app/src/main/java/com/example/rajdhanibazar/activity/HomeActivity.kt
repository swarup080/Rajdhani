package com.example.rajdhanibazar.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel("https://bit.ly/3fLJf72", "And people do that."))
        imageList.add(SlideModel("https://bit.ly/3fLJf72", "And people do that."))
        imageList.add(SlideModel("https://bit.ly/3fLJf72", "And people do that."))

        binding.imageSlider.setImageList(imageList)



    }
}
