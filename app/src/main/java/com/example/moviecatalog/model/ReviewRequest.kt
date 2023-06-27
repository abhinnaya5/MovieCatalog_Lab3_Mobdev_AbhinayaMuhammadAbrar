package com.example.moviecatalog.model

data class ReviewRequest(
    val isAnonymous: Boolean,
    val rating: Int,
    val reviewText: String
)
