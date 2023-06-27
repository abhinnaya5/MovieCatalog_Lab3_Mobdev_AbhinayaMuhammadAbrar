package com.example.moviecatalog.model

data class MoviesResponse(
    val movies: List<MoviesItem>,
    val pageInfo: PageInfo
)

data class ReviewsItem(
    val rating: Int,
    val id: String,

    val isAnonymous: Boolean? = null,
    val author: Author? = null,
    val reviewText: String? = null,
    val createDateTime: String? = null
)

data class MoviesItem(
    val country: String,
    val reviews: List<ReviewsItem>,
    val year: Int,
    val genres: List<GenresItem>,
    val name: String,
    val id: String,
    val poster: String,

    val fees: Int? = null,
    val director: String? = null,
    val description: String? = null,
    val ageLimit: Int? = null,
    val tagline: String? = null,
    val time: Int? = null,
    val budget: Int? = null
)

data class PageInfo(
    val pageCount: Int,
    val pageSize: Int,
    val currentPage: Int
)

data class GenresItem(
    val name: String,
    val id: String
)

data class Author(
    val nickName: String,
    val avatar: String,
    val userId: String
)
