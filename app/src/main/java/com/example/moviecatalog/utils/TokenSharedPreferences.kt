package com.example.moviecatalog.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.moviecatalog.utils.Constant.TOKEN

class TokenSharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE)

    fun setToken(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String {
        return sharedPreferences.getString(TOKEN, "") ?: ""
    }

    fun removeToken() {
        sharedPreferences.edit().remove(TOKEN).apply()
    }
}