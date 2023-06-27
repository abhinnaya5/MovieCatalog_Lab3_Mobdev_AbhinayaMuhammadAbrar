package com.example.moviecatalog.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalog.databinding.ActivitySignInBinding
import com.example.moviecatalog.model.AuthRequest
import com.example.moviecatalog.utils.TokenSharedPreferences
import com.example.moviecatalog.viewmodel.UserViewModel

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var tokenSharedPreferences: TokenSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        tokenSharedPreferences = TokenSharedPreferences(this)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.buttonSignin.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            viewModel.loginUser(AuthRequest(userName = username, password = password))
            viewModel.authResponse.observe(this) { item ->
                if (item?.token != null) {
                    tokenSharedPreferences.setToken(item.token)
                    startActivity(Intent(this@SignInActivity, DashboardActivity::class.java))
                    finish()
                }
            }

        }

        binding.buttonSignup.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }
    }
}