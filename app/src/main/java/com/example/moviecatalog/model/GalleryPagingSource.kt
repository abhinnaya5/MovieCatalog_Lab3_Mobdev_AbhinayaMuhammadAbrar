package com.example.moviecatalog.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryPagingSource(private val retrofitService: RetrofitService) :
    PagingSource<Int, MoviesItem>() {
    override fun getRefreshKey(state: PagingState<Int, MoviesItem>): Int? {
        return state.anchorPosition?.let { position ->
            val anchor = state.closestPageToPosition(position)
            anchor?.prevKey?.plus(INITIAL_PAGE) ?: anchor?.nextKey?.minus(INITIAL_PAGE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesItem> {
        val page = params.key ?: INITIAL_PAGE

        return try {
            val response = withContext(Dispatchers.IO) { retrofitService.getMovies(page).execute() }
            if (response.isSuccessful) {
                val responseBody = response.body()
                val movies = responseBody?.movies ?: emptyList()

                if (movies.isEmpty()) {
                    LoadResult.Error(java.lang.Exception("Error loading movies"))
                } else {
                    val prevKey = if (page > 1) page - 1 else null
                    val nextKey = page + 1

                    LoadResult.Page(
                        data = movies,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
            } else {
                LoadResult.Error(Exception("Error loading movies"))
            }
        } catch (error: Exception) {
            LoadResult.Error(error)
        }
    }

    companion object {
        const val INITIAL_PAGE = 1
    }
}