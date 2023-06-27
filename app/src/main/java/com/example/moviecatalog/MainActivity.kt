package com.example.moviecatalog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviecatalog.utils.TokenSharedPreferences
import com.example.moviecatalog.view.DashboardActivity
import com.example.moviecatalog.view.SignInActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tokenSharedPreferences: TokenSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenSharedPreferences = TokenSharedPreferences(this)
        setContentView(R.layout.activity_main)

        if (tokenSharedPreferences.getToken() == "") {
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
            finish()
        }
    }
}