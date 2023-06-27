package com.example.moviecatalog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalog.R
import com.example.moviecatalog.databinding.FragmentReviewDialogBinding
import com.example.moviecatalog.model.ReviewRequest
import com.example.moviecatalog.utils.Constant
import com.example.moviecatalog.utils.TokenSharedPreferences
import com.example.moviecatalog.viewmodel.MovieViewModel

class ReviewDialogFragment : Fragment() {
    private var _binding: FragmentReviewDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MovieViewModel
    private lateinit var tokenSharedPreferences: TokenSharedPreferences
    private lateinit var movieId: String
    private var reviewId: String? = null
    private var isAnonymous: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewDialogBinding.inflate(inflater, null, false)
        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        tokenSharedPreferences = TokenSharedPreferences(requireContext())
        movieId = arguments?.getString(Constant.MOVIE_ID).toString()
        reviewId = arguments?.getString(Constant.REVIEW_ID).toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = tokenSharedPreferences.getToken()
        var rating = 0

        viewModel.getMovieDetails(movieId)
        viewModel.detailData.observe(viewLifecycleOwner) { item ->
            item?.reviews?.forEach {
                if (it.id == reviewId) {
                    rating = it.rating
                    displayStar(rating)
                    binding.editReview.setText(it.reviewText)
                    isAnonymous = it.isAnonymous!!
                    if (it.isAnonymous == true) {
                        binding.editAnonymous.setImageResource(R.drawable.border_anonymous_clicked)
                    } else {
                        binding.editAnonymous.setImageResource(R.drawable.border_anonymous)
                    }
                }
            }
        }

        binding.imageStar1.setOnClickListener {
            rating = 1
            displayStar(rating)
        }
        binding.imageStar2.setOnClickListener {
            rating = 2
            displayStar(rating)
        }
        binding.imageStar3.setOnClickListener {
            rating = 3
            displayStar(rating)
        }
        binding.imageStar4.setOnClickListener {
            rating = 4
            displayStar(rating)
        }
        binding.imageStar5.setOnClickListener {
            rating = 5
            displayStar(rating)
        }
        binding.imageStar6.setOnClickListener {
            rating = 6
            displayStar(rating)
        }
        binding.imageStar7.setOnClickListener {
            rating = 7
            displayStar(rating)
        }
        binding.imageStar8.setOnClickListener {
            rating = 8
            displayStar(rating)
        }
        binding.imageStar9.setOnClickListener {
            rating = 9
            displayStar(rating)
        }
        binding.imageStar10.setOnClickListener {
            rating = 10
            displayStar(rating)
        }

        binding.editAnonymous.setOnClickListener {
            isAnonymous = !isAnonymous
            if (isAnonymous) {
                binding.editAnonymous.setImageResource(R.drawable.border_anonymous_clicked)
            } else {
                binding.editAnonymous.setImageResource(R.drawable.border_anonymous)
            }
        }

        binding.buttonSave.setOnClickListener {
            if (reviewId!!.length < 5) {
                if (rating > 0 && binding.editReview.text != null) {
                    val request = ReviewRequest(
                        isAnonymous = isAnonymous,
                        reviewText = binding.editReview.text.toString(),
                        rating = rating
                    )
                    viewModel.addReview(token, movieId, request)
                    viewModel.responseCode.observe(viewLifecycleOwner) { code ->
                        if (code == 200) {
                            Toast.makeText(
                                requireContext(),
                                "Successfully added review",
                                Toast.LENGTH_SHORT
                            ).show()
                            activity?.supportFragmentManager?.beginTransaction()?.remove(this)
                                ?.commit()
                            activity?.recreate()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed adding review",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            } else {
                val request = ReviewRequest(
                    isAnonymous = isAnonymous,
                    reviewText = binding.editReview.text.toString(),
                    rating = rating
                )
                viewModel.editReview(token, movieId, reviewId!!, request)
                viewModel.responseCode.observe(viewLifecycleOwner) { code ->
                    if (code == 200) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully edited review",
                            Toast.LENGTH_SHORT
                        ).show()
                        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                        activity?.recreate()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed editing review",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

        }
    }

    private fun displayStar(rating: Int) {
        for (i in 1..rating) {
            val starId =
                resources.getIdentifier("image_star_$i", "id", requireContext().packageName)
            val starImage = binding.root.findViewById<ImageView>(starId)

            starImage.setImageResource(R.drawable.ic_star_clicked)
        }
        for (j in rating + 1..10) {
            val starId =
                resources.getIdentifier("image_star_$j", "id", requireContext().packageName)
            val starImage = binding.root.findViewById<ImageView>(starId)

            starImage.setImageResource(R.drawable.ic_star)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(movieId: String, reviewId: String?): ReviewDialogFragment {
            val fragment = ReviewDialogFragment()
            val args = Bundle()
            args.putString(Constant.MOVIE_ID, movieId)
            args.putString(Constant.REVIEW_ID, reviewId)
            fragment.arguments = args
            return fragment
        }
    }

}