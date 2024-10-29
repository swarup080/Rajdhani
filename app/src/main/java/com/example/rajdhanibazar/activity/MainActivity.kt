package com.example.rajdhanibazar.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rajdhanibazar.R
import com.example.rajdhanibazar.adapter.MarketAdapter
import com.example.rajdhanibazar.authActivity.SignUpActivity
import com.example.rajdhanibazar.data.FetchUser
import com.example.rajdhanibazar.data.Market
import com.example.rajdhanibazar.databinding.ActivityMainBinding

import com.example.rajdhanibazar.retrofit.RetrofitInstance
import com.example.rajdhanibazar.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var marketAdapter: MarketAdapter
    private lateinit var marketList: List<Market>
    private lateinit var userViewModel: UserViewModel

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            onMenuItemSelected(menuItem)
            true
        }

        // Navigation and contact info setup
        navigation()
        contactInfo()

        // Fetch data into RecyclerView
        marketInfo()

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val phoneNumber = intent.getStringExtra("phoneNumber")

        if (phoneNumber != null) {
            val sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("phoneNumber", phoneNumber)
            editor.apply()
        }

        val sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
        val savedPhoneNumber = sharedPreferences.getString("phoneNumber", null)

        // Use savedPhoneNumber as needed
        if (savedPhoneNumber != null) {
            // Use the phone number
            userViewModel.fetchUser(savedPhoneNumber)
        }

        accountBalance()
    }

    private fun navigation() {
        binding.starlineGames.setOnClickListener {
            startActivity(Intent(this, StarLineGamesActivity::class.java))
        }
        binding.galiGames.setOnClickListener {
            startActivity(Intent(this, GaliGamesActivity::class.java))
        }
    }

    private fun contactInfo() {
        binding.whatsappContact.setOnClickListener {
            val url = "https://wa.me/+916367223825"
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.setPackage("com.whatsapp")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Whatsapp is not installed in your phone", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.callContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+916367223825")
            startActivity(intent)
        }
    }

    private fun onMenuItemSelected(menuItem: MenuItem) {
        menuItem.isChecked = true
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        when (menuItem.itemId) {
            R.id.profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.wallet -> startActivity(Intent(this, WalletActivity::class.java))
            R.id.add_fund -> startActivity(Intent(this, AddFundActivity::class.java))
            R.id.withdarw -> startActivity(Intent(this, WithdrawFundActivity::class.java))
           // R.id.bank_details -> startActivity(Intent(this, BankDetailsActivity::class.java))
            R.id.winHistory -> startActivity(Intent(this, WinAndBidHistoryActivity::class.java))
            R.id.bidHistory -> startActivity(Intent(this, WinAndBidHistoryActivity::class.java))
            R.id.howToPlay -> startActivity(Intent(this, HTPlayActivity::class.java))
            R.id.rate -> startActivity(Intent(this, GameRatesActivity::class.java))
            R.id.notice -> startActivity(Intent(this, NoticeActivity::class.java))
            R.id.contact -> startActivity(Intent(this, ContactActivity::class.java))
            R.id.share -> {
                // Handle the share navigation item
            }

            R.id.rating -> Toast.makeText(this, "Currently not working", Toast.LENGTH_SHORT).show()
            R.id.logout -> logout()
            else -> Toast.makeText(this, "Password change is not available now", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun logout() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("QuickCartPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        val intent = Intent(this, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun marketInfo() {
        RetrofitInstance.apiInterface.getMarkets().enqueue(object : Callback<List<Market>?> {
            override fun onResponse(call: Call<List<Market>?>, response: Response<List<Market>?>) {
                if (response.isSuccessful) {
                    marketList = response.body()?.sortedBy { it.openTime } ?: emptyList()  // Sort by open time
                    if (marketList.isNotEmpty()) {
                        marketAdapter = MarketAdapter(this@MainActivity, marketList)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                        binding.recyclerView.adapter = marketAdapter
                    } else {
                        Toast.makeText(this@MainActivity, "No markets available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch markets", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Market>?>, t: Throwable) {
                Log.e(TAG, "Error fetching markets", t)
                Toast.makeText(this@MainActivity, "Error fetching markets", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun accountBalance() {
        userViewModel.user.observe(this) { user ->
            if (user != null) {
                Log.d("MainActivity", "User balance: ${user.balance}")

                // Format balance to "x.xk" if it is 1000 or more
                val formattedBalance = if (user.balance >= 1000) {
                    String.format("%.2fk", user.balance / 1000.0)
                } else {
                    user.balance.toString()
                }

                binding.accountBalance.text = formattedBalance
                saveUserDetails(user)
            } else {
                Log.d("MainActivity", "User not found")
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveUserDetails(user: FetchUser) {
        Log.d("MainActivity", "Saving user details: ${user.name}, ${user.email}")
        val sharedPreferences = getSharedPreferences("RajdhaniBazarPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userName", user.name)
        editor.putString("userEmail", user.email)
        editor.putInt("userBalance", user.balance)
        editor.apply()
    }
}
