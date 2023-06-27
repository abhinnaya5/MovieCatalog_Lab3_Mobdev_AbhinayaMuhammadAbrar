package com.example.moviecatalog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviecatalog.databinding.FragmentMainBinding
import com.example.moviecatalog.model.MoviesItem
import com.example.moviecatalog.utils.*
import com.example.moviecatalog.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment(), FavoriteAdapterClickListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MovieViewModel
    private lateinit var tokenSharedPreferences: TokenSharedPreferences
    private val galleryAdapter: GalleryPagingAdapter by lazy {
        GalleryPagingAdapter(object : GalleryAdapterClickListener {
            override fun displayMovie(movie: MoviesItem) {
                displayHighlight(movie)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, null, false)
        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        tokenSharedPreferences = TokenSharedPreferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = tokenSharedPreferences.getToken()
        viewModel.getGalleryMovies(1)

        binding.recyclerFavorites.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerGallery.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val favoriteMovies = ArrayList<MoviesItem>()
        viewModel.getFavoriteMovies(token)
        viewModel.favoriteData.observe(this) { item ->
            if (item != null) {
                if (item.movies.isEmpty()) {
                    binding.textFavorites.visibility = View.GONE
                    binding.recyclerFavorites.visibility = View.GONE
                } else {
                    item.movies.let { moviesList ->
                        favoriteMovies.clear()
                        favoriteMovies.addAll(moviesList)
                        val favoriteAdapter = FavoriteAdapter(favoriteMovies, this)
                        binding.recyclerFavorites.adapter = favoriteAdapter
                    }
                }
            }
        }

        binding.recyclerGallery.adapter = galleryAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPagingMovies().collectLatest {
                galleryAdapter.submitData(it)
            }
        }

//        val galleryMovies = ArrayList<MoviesItem>()
//        viewModel.getGalleryMovies(1)
//        viewModel.galleryData.observe(this) { item ->
//            item?.movies?.let { moviesList ->
//                galleryMovies.clear()
//                galleryMovies.addAll(moviesList.subList(1, moviesList.size))
//
//                Glide.with(this)
//                    .load(moviesList[0].poster)
//                    .into(binding.imageMoviePoster)
//            }
//            val galleryAdapter = GalleryAdapter(galleryMovies)
//            binding.recyclerGallery.adapter = galleryAdapter
//        }
    }

    private fun displayHighlight(movie: MoviesItem) {
        binding.textMovieTitle.text = movie.name
        Glide.with(this)
            .load(movie.poster)
            .into(binding.imageMoviePoster)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun deleteFavoriteMovie(movieId: String) {
        viewModel.deleteFavoriteMovie(tokenSharedPreferences.getToken(), movieId)
        viewModel.responseCode.observe(this) {
            if (it == 200) {
                Toast.makeText(requireContext(), "Deleted a favorite movie", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Failed deleting a favorite movie",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}