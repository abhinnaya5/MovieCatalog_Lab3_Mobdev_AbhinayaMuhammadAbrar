package com.example.moviecatalog.utils

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.databinding.ListGalleryBinding
import com.example.moviecatalog.model.MoviesItem
import com.example.moviecatalog.view.MovieActivity
import com.google.android.flexbox.FlexboxLayout

class GalleryAdapter(private val listMovies: ArrayList<MoviesItem>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    class ViewHolder(binding: ListGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        private val poster = binding.imageMoviePoster
        private val title = binding.textMovieTitle
        private val year = binding.textMovieYear
        private val country = binding.textMovieCountry
        private val rating = binding.textRating
        private val genre = binding.textGenre

        fun bind(data: MoviesItem) {
            Glide.with(itemView)
                .load(data.poster)
                .into(poster)

            title.text = data.name
            year.text = data.year.toString()
            country.text = data.country

            val layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            data.genres.forEachIndexed { index, genresItem ->
                val textGenre = TextView(itemView.context)
                textGenre.text = genresItem.name

                if (index != data.genres.size - 1) textGenre.append(", ")
                textGenre.setTextColor(itemView.resources.getColor(R.color.white))
                genre.addView(textGenre, layoutParams)
            }

            var averageRating = 0F
            data.reviews.forEach { averageRating += it.rating }
            averageRating /= data.reviews.size
            rating.text = String.format("%.1f", averageRating)

            val red = (255 * (10 - averageRating) / 10).toInt()
            val green = (255 * averageRating / 10).toInt()
            val color = Color.rgb(red, green, 0)

            rating.background.setTint(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listMovies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMovies[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MovieActivity::class.java)
            intent.putExtra(Constant.MOVIE_ID, listMovies[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }
}