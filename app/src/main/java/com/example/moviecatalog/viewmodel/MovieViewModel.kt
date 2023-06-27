package com.example.moviecatalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviecatalog.model.*
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val _galleryData = MutableLiveData<MoviesResponse?>()
    val galleryData: LiveData<MoviesResponse?> get() = _galleryData

    private val _favoriteData = MutableLiveData<MoviesResponse?>()
    val favoriteData: LiveData<MoviesResponse?> get() = _favoriteData

    private val _detailData = MutableLiveData<MoviesItem?>()
    val detailData: LiveData<MoviesItem?> get() = _detailData

    private val _basicResponse = MutableLiveData<ResponseBody?>()
    val basicResponse: LiveData<ResponseBody?> get() = _basicResponse

    private val _responseCode = MutableLiveData<Int?>()
    val responseCode: LiveData<Int?> get() = _responseCode

    fun getGalleryMovies(page: Int) {
        RetrofitConfig.getRetrofitService().getMovies(page)
            .enqueue(object : Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    response: Response<MoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        _galleryData.value = response.body()
                    } else {
                        _galleryData.value = null
                    }
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    _galleryData.value = null
                }
            })
    }

    fun getPagingMovies(): Flow<PagingData<MoviesItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = false),
            pagingSourceFactory = { GalleryPagingSource(RetrofitConfig.getRetrofitService()) }
        ).flow
    }

    fun getMovieDetails(id: String) {
        RetrofitConfig.getRetrofitService().getMovieDetails(id)
            .enqueue(object : Callback<MoviesItem> {
                override fun onResponse(
                    call: Call<MoviesItem>,
                    response: Response<MoviesItem>
                ) {
                    if (response.isSuccessful) {
                        _detailData.value = response.body()
                    } else {
                        _detailData.value = null
                    }
                }

                override fun onFailure(call: Call<MoviesItem>, t: Throwable) {
                    _detailData.value = null
                }
            })
    }

    fun getFavoriteMovies(token: String) {
        RetrofitConfig.getRetrofitService().getFavorites("Bearer $token")
            .enqueue(object : Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    response: Response<MoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        _favoriteData.value = response.body()
                    } else {
                        _favoriteData.value = null
                    }
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    _favoriteData.value = null
                }
            })
    }

    fun deleteFavoriteMovie(token: String, id: String) {
        RetrofitConfig.getRetrofitService().deleteFavorites("Bearer $token", id)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        _basicResponse.value = response.body()
                        _responseCode.value = response.code()
                    } else {
                        _basicResponse.value = null
                        _responseCode.value = response.code()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _basicResponse.value = null
                }
            })
    }

    fun addReview(token: String, movieId: String, review: ReviewRequest) {
        RetrofitConfig.getRetrofitService().addReview("Bearer $token", movieId, review)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        _basicResponse.value = response.body()
                        _responseCode.value = response.code()
                    } else {
                        _basicResponse.value = null
                        _responseCode.value = response.code()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _basicResponse.value = null
                }
            })
    }

    fun editReview(token: String, movieId: String, reviewId: String, review: ReviewRequest) {
        RetrofitConfig.getRetrofitService().editReview("Bearer $token", movieId, reviewId, review)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        _basicResponse.value = response.body()
                        _responseCode.value = response.code()
                    } else {
                        _basicResponse.value = null
                        _responseCode.value = response.code()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _basicResponse.value = null
                }
            })
    }

    fun deleteReview(token: String, movieId: String, reviewId: String) {
        RetrofitConfig.getRetrofitService().deleteReview("Bearer $token", movieId, reviewId)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        _basicResponse.value = response.body()
                        _responseCode.value = response.code()
                    } else {
                        _basicResponse.value = null
                        _responseCode.value = response.code()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _basicResponse.value = null
                }
            })
    }
}