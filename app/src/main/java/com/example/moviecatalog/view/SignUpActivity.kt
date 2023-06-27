package com.example.moviecatalog.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalog.R
import com.example.moviecatalog.databinding.ActivitySignUpBinding
import com.example.moviecatalog.model.AuthRequest
import com.example.moviecatalog.utils.TokenSharedPreferences
import com.example.moviecatalog.viewmodel.UserViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var tokenSharedPreferences: TokenSharedPreferences
    private var gender: Int? = null
    private var birthday: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        tokenSharedPreferences = TokenSharedPreferences(this)

        val name = binding.editName.text
        val username = binding.editUsername.text
        val email = binding.editEmail.text
        val password = binding.editPassword.text
        val repeatPassword = binding.editRepeatPassword.text

        binding.buttonMale.setOnClickListener {
            binding.buttonMale.background =
                AppCompatResources.getDrawable(this, R.drawable.border_male_clicked)
            binding.buttonMale.setTextColor(getColor(R.color.white))

            binding.buttonFemale.background =
                AppCompatResources.getDrawable(this, R.drawable.border_female)
            binding.buttonFemale.setTextColor(getColor(R.color.gray_faded))
            gender = 1
        }

        binding.buttonFemale.setOnClickListener {
            binding.buttonFemale.background =
                AppCompatResources.getDrawable(this, R.drawable.border_female_clicked)
            binding.buttonFemale.setTextColor(getColor(R.color.white))

            binding.buttonMale.background =
                AppCompatResources.getDrawable(this, R.drawable.border_male)
            binding.buttonMale.setTextColor(getColor(R.color.gray_faded))
            gender = 2
        }

        binding.buttonSignup.setOnClickListener {
            val user = AuthRequest(
                name = name.toString(),
                userName = username.toString(),
                email = email.toString(),
                gender = gender!!,
                password = password.toString(),
                birthDate = birthday
            )
            viewModel.registerUser(user)
            viewModel.authResponse.observe(this) {
                if (it?.token != null) {
                    tokenSharedPreferences.setToken(it.token)
                    startActivity(Intent(this@SignUpActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SignUpActivity, "Failed to sign up", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        binding.buttonSignin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
        }
    }

    fun showCalendar(view: View) {
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()

        datePicker.show(supportFragmentManager, "datePicker")

        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it

            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val serverDateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            birthday = serverDateFormat.format(calendar.time).toString()

            binding.editBirthday.text = simpleDateFormat.format(calendar.time).toString()
            binding.editBirthday.setTextColor(getColor(R.color.orange))
        }
    }
}