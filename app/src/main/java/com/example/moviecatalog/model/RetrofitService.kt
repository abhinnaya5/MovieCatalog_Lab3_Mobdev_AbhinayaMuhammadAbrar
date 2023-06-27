package com.example.moviecatalog.model

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @POST("account/login")
    fun loginUser(
        @Body user: AuthRequest
    ): Call<AuthResponse>

    @POST("account/register")
    fun registerUser(
        @Body user: AuthRequest
    ): Call<AuthResponse>

    @POST("account/logout")
    fun logoutUser(): Call<ResponseBody>

    @GET("movies/{page}")
    fun getMovies(
        @Path("page") page: Int
    ): Call<MoviesResponse>

    @GET("movies/details/{id}")
    fun getMovieDetails(
        @Path("id") id: String
    ): Call<MoviesItem>

    @GET("account/profile")
    fun getProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @PUT("account/profile")
    fun editProfile(
        @Header("Authorization") token: String,
        @Body user: ProfileResponse
    ): Call<ResponseBody>

    @GET("favorites")
    fun getFavorites(
        @Header("Authorization") token: String
    ): Call<MoviesResponse>

    @POST("favorites/{id}/add")
    fun addFavorites(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<ResponseBody>

    @DELETE("favorites/{id}/delete")
    fun deleteFavorites(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<ResponseBody>

    @POST("movie/{movieId}/review/add")
    fun addReview(
        @Header("Authorization") token: String,
        @Path("movieId") movieId: String,
        @Body review: ReviewRequest
    ): Call<ResponseBody>

    @PUT("movie/{movieId}/review/{id}/edit")
    fun editReview(
        @Header("Authorization") token: String,
        @Path("movieId") movieId: String,
        @Path("id") id: String,
        @Body review: ReviewRequest
    ): Call<ResponseBody>

    @DELETE("movie/{movieId}/review/{id}/delete")
    fun deleteReview(
        @Header("Authorization") token: String,
        @Path("movieId") movieId: String,
        @Path("id") id: String,
    ): Call<ResponseBody>
}
