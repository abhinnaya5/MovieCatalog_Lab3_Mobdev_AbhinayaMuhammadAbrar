package com.example.moviecatalog.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.databinding.ListReviewBinding
import com.example.moviecatalog.model.ReviewsItem
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter(
    private val listReview: ArrayList<ReviewsItem>,
    private val userId: String,
    private val movieId: String,
    private val listener: ReviewAdapterClickListener
) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    class ViewHolder(binding: ListReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val avatar = binding.imageProfile
        private val username = binding.editUsername
        private val review = binding.textReview
        private val date = binding.textDate
        private val rating = binding.textRating
        val pencilButton = binding.buttonEditReview
        val crossButton = binding.buttonDeleteReview
        private val myReview = binding.textMyReview

        fun bind(data: ReviewsItem, userId: String) {
            if (data.isAnonymous == true) {
                Glide.with(itemView)
                    .load(itemView.context.getDrawable(R.drawable.ic_user))
                    .into(avatar)

                username.text = itemView.context.getString(R.string.anonymous)
            } else {
                Glide.with(itemView)
                    .load(data.author?.avatar)
                    .into(avatar)

                username.text = data.author?.nickName
            }
            review.text = data.reviewText

            try {
                val serverDateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val dateCreated = serverDateFormat.parse(data.createDateTime.toString())
                date.text = dateCreated?.let { simpleDateFormat.format(it) }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val red = (255 * (10 - data.rating) / 10)
            val green = (255 * data.rating / 10)
            val color = Color.rgb(red, green, 0)

            rating.text = data.rating.toString()
            rating.background.setTint(color)

            if (data.author != null && data.author.userId == userId) {
                pencilButton.visibility = View.VISIBLE
                crossButton.visibility = View.VISIBLE
                myReview.visibility = View.VISIBLE
            } else {
                pencilButton.visibility = View.GONE
                crossButton.visibility = View.GONE
                myReview.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listReview.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listReview[position], userId)

        holder.pencilButton.setOnClickListener {
            listener.editReview(
                movieId,
                listReview[position].id
            )
        }

        holder.crossButton.setOnClickListener {
            listener.deleteReview(
                movieId,
                listReview[position].id
            )
        }
    }
}

interface ReviewAdapterClickListener {
    fun editReview(movieId: String, reviewId: String)

    fun deleteReview(movieId: String, reviewId: String)
}