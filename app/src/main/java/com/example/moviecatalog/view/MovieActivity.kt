package com.example.moviecatalog.view

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.databinding.ActivityMovieBinding
import com.example.moviecatalog.model.ReviewsItem
import com.example.moviecatalog.utils.Constant
import com.example.moviecatalog.utils.ReviewAdapter
import com.example.moviecatalog.utils.ReviewAdapterClickListener
import com.example.moviecatalog.utils.TokenSharedPreferences
import com.example.moviecatalog.viewmodel.MovieViewModel
import com.example.moviecatalog.viewmodel.UserViewModel
import com.google.android.flexbox.FlexboxLayout

class MovieActivity : AppCompatActivity(), ReviewAdapterClickListener {
    private lateinit var binding: ActivityMovieBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var tokenSharedPreferences: TokenSharedPreferences
    private var reviewDialogFragment: ReviewDialogFragment? = null
    private lateinit var movieId: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        tokenSharedPreferences = TokenSharedPreferences(this)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        supportActionBar?.title = null

        val flags =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = flags
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

        movieId = intent.getStringExtra(Constant.MOVIE_ID).toString()
        val token = tokenSharedPreferences.getToken()
        userViewModel.getProfile(token)
        userViewModel.profileResponse.observe(this) { if (it?.id != null) userId = it.id }

        viewModel.getMovieDetails(movieId)
        viewModel.detailData.observe(this) { item ->
            if (item != null) {
                Glide.with(this).load(item.poster).into(binding.imageMoviePoster)

                binding.textMovieTitle.text = item.name
                binding.textMovieDescription.text = item.description

                binding.editYear.text = item.year.toString()
                binding.editCountry.text = item.country
                binding.editDuration.text = String.format("%d мин.", item.time)
                binding.editDirector.text = item.director
                binding.editAgeLimit.text = String.format("%d+", item.ageLimit)

                val budget = if (item.budget == null) {
                    String.format("-")
                } else {
                    String.format("$%s", item.budget)
                }
                binding.editBudget.text = budget

                val fees = if (item.fees == null) {
                    String.format("-")
                } else {
                    String.format("$%s", item.fees)
                }
                binding.editFees.text = fees

                val layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT
                )

                item.genres.forEachIndexed { index, genresItem ->
                    val textGenre = TextView(this)
                    textGenre.text = ""
                    textGenre.text = genresItem.name
                    textGenre.setPadding(16.dpToPx(), 6.dpToPx(), 16.dpToPx(), 6.dpToPx())
                    textGenre.background =
                        AppCompatResources.getDrawable(this, R.drawable.border_orange)
                    textGenre.setTextColor(getColor(R.color.white))
                    layoutParams.setMargins(0.dpToPx(), 8.dpToPx(), 8.dpToPx(), 0.dpToPx())
                    binding.editGenre.addView(textGenre, layoutParams)
                }

                val reviewItem = ArrayList<ReviewsItem>()
                sortHasOwnReview(item.reviews).let {
                    reviewItem.clear()
                    reviewItem.addAll(it)
                }
                val reviewAdapter = ReviewAdapter(reviewItem, userId, movieId, this)
                binding.recyclerReview.adapter = reviewAdapter
            }
        }

        binding.buttonAddReview.setOnClickListener {
            reviewDialogFragment = ReviewDialogFragment.newInstance(movieId, null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.review_dialog, reviewDialogFragment!!).commit()

            binding.reviewDialogBackground.visibility = View.VISIBLE

        }
        binding.reviewDialogBackground.setOnClickListener {
            reviewDialogFragment?.let { fragment ->
                supportFragmentManager.beginTransaction().remove(fragment).commit()

                binding.reviewDialogBackground.visibility = View.GONE
                binding.reviewDialogBackground.setOnTouchListener(null)
            }
        }
    }

    private fun sortHasOwnReview(
        reviews: List<ReviewsItem>
    ): List<ReviewsItem> {
        val matchedReviews = mutableListOf<ReviewsItem>()
        val remainingReviews = mutableListOf<ReviewsItem>()
        var isUserIdMatched = false

        for (review in reviews) {
            if (review.author != null && review.author.userId == userId) {
                matchedReviews.add(review)
                isUserIdMatched = true
            } else {
                remainingReviews.add(review)
            }
        }
        val sortedReviews = if (isUserIdMatched) {
            matchedReviews + remainingReviews
        } else {
            reviews
        }
        return sortedReviews
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun editReview(movieId: String, reviewId: String) {
        reviewDialogFragment = ReviewDialogFragment.newInstance(movieId, reviewId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.review_dialog, reviewDialogFragment!!).commit()

        binding.reviewDialogBackground.visibility = View.VISIBLE

        binding.reviewDialogBackground.setOnClickListener {
            reviewDialogFragment?.let { fragment ->
                supportFragmentManager.beginTransaction().remove(fragment).commit()

                binding.reviewDialogBackground.visibility = View.GONE
                binding.reviewDialogBackground.setOnTouchListener(null)
            }
        }
    }

    override fun deleteReview(movieId: String, reviewId: String) {
        viewModel.deleteReview(tokenSharedPreferences.getToken(), movieId, reviewId)
        viewModel.responseCode.observe(this) { code ->
            if (code == 200) {
                Toast.makeText(this, "Successfully deleted a review", Toast.LENGTH_SHORT).show()
                recreate()
            } else {
                Toast.makeText(this, "Failed deleting a review", Toast.LENGTH_SHORT).show()
            }
        }
    }

}