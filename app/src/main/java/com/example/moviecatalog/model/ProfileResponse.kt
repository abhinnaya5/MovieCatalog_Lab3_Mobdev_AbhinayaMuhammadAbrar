package com.example.moviecatalog.model

data class ProfileResponse(
    val gender: Int,
    val nickName: String,
    val avatarLink: String? = null,
    val name: String,
    val id: String,
    val birthDate: String,
    val email: String
)
