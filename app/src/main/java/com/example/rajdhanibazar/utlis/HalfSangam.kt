package com.example.rajdhanibazar.utlis

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioButton
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.databinding.ActivityHalfSangamBinding

class HalfSangam : AppCompatActivity() {
    private lateinit var binding: ActivityHalfSangamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHalfSangamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define the colors for selected and unselected states
        val blackColor = resources.getColor(R.color.black, theme)
        val whiteColor = resources.getColor(android.R.color.white, theme)

        // Create a ColorStateList for the button tint (for selected and unselected states)
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked), // Selected state
                intArrayOf(-android.R.attr.state_checked)  // Unselected state
            ),
            intArrayOf(
                whiteColor, // White when selected
                blackColor  // Black when unselected
            )
        )

        // Apply the ColorStateList to all RadioButtons in the RadioGroup
        for (i in 0 until binding.radioGroup.childCount) {
            val radioButton = binding.radioGroup.getChildAt(i) as? RadioButton
            radioButton?.buttonTintList = colorStateList
        }

        // This data comes from the game adapter
        val gameName = intent.getStringExtra("gameName")
        binding.gamePlayName.text = gameName

        // Set OnClickListener for the submit button
        binding.save.setOnClickListener {
            // Get the selected radio button ID and text
            val selectedRadioButtonId = binding.radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                val selectedOption = selectedRadioButton.text.toString()

                // Navigate based on selected option
                when (selectedOption) {
                    "Open" -> {
                        val intent = Intent(this, HalfSangamOpen::class.java)
                        intent.putExtra("gameName", gameName)
                        intent.putExtra("selectedOption", selectedOption)
                        startActivity(intent)
                    }
                    "Close" -> {
                        val intent = Intent(this, HalfSangamClose::class.java)
                        intent.putExtra("gameName", gameName)
                        intent.putExtra("selectedOption", selectedOption)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
