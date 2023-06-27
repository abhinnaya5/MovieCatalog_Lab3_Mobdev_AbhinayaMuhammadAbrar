package com.example.moviecatalog.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalog.databinding.ListFavoriteBinding
import com.example.moviecatalog.model.MoviesItem
import com.example.moviecatalog.view.MovieActivity

class FavoriteAdapter(
    private val listFavorite: ArrayList<MoviesItem>,
    private val listener: FavoriteAdapterClickListener
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    class ViewHolder(binding: ListFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val poster = binding.imageMoviePoster
        val deleteButton = binding.buttonDeleteFavorite

        fun bind(data: MoviesItem) {
            Glide.with(itemView)
                .load(data.poster)
                .into(poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavorite[position])

        holder.deleteButton.setOnClickListener {
            listener.deleteFavoriteMovie(listFavorite[position].id)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MovieActivity::class.java)
            intent.putExtra(Constant.MOVIE_ID, listFavorite[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }
}

interface FavoriteAdapterClickListener {
    fun deleteFavoriteMovie(movieId: String)
}