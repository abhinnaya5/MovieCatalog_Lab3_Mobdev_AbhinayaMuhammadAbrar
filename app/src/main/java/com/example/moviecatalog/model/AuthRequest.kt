package com.example.moviecatalog.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    val password: String? = null,
    val gender: Int? = null,
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("userName")
    val userName: String? = null,

    val birthDate: String? = null,
    val email: String? = null
)
