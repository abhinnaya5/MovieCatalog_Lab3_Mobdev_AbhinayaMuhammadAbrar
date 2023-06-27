package com.example.moviecatalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalog.model.AuthRequest
import com.example.moviecatalog.model.AuthResponse
import com.example.moviecatalog.model.ProfileResponse
import com.example.moviecatalog.model.RetrofitConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val _authResponse = MutableLiveData<AuthResponse?>()
    val authResponse: LiveData<AuthResponse?> get() = _authResponse

    private val _basicResponse = MutableLiveData<ResponseBody?>()
    val basicResponse: LiveData<ResponseBody?> get() = _basicResponse

    private val _responseCode = MutableLiveData<Int?>()
    val responseCode: LiveData<Int?> get() = _responseCode

    private val _profileResponse = MutableLiveData<ProfileResponse?>()
    val profileResponse: LiveData<ProfileResponse?> get() = _profileResponse

    fun loginUser(user: AuthRequest) {
        RetrofitConfig.getRetrofitService().loginUser(user)
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful) {
                        _authResponse.value = response.body()
                    } else {
                        _authResponse.value = null
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    _authResponse.value = null
                }
            })
    }

    fun registerUser(user: AuthRequest) {
        RetrofitConfig.getRetrofitService().registerUser(user)
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful) {
                        _authResponse.value = response.body()
                    } else {
                        _authResponse.value = null
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    _authResponse.value = null
                }
            })
    }

    fun logoutUser() {
        RetrofitConfig.getRetrofitService().logoutUser().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _basicResponse.value = response.body()
                } else {
                    _basicResponse.value = null
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _basicResponse.value = null
            }
        })
    }

    fun getProfile(token: String) {
        RetrofitConfig.getRetrofitService().getProfile("Bearer $token")
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        _profileResponse.value = response.body()
                    } else {
                        _profileResponse.value = null
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    _profileResponse.value = null
                }
            })
    }

    fun editProfile(token: String, user: ProfileResponse) {
        RetrofitConfig.getRetrofitService().editProfile("Bearer $token", user)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        _responseCode.value = response.code()
                        _basicResponse.value = response.body()
                    } else {
                        _basicResponse.value = null
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _basicResponse.value = null
                }
            })
    }

}