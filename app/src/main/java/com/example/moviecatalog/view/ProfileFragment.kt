package com.example.moviecatalog.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.databinding.FragmentProfileBinding
import com.example.moviecatalog.model.ProfileResponse
import com.example.moviecatalog.utils.TokenSharedPreferences
import com.example.moviecatalog.viewmodel.UserViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel
    private lateinit var tokenSharedPreferences: TokenSharedPreferences
    private var birthDate: String? = null
    private var dateFormat: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, null, false)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        tokenSharedPreferences = TokenSharedPreferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = tokenSharedPreferences.getToken()
        var gender = 0
        var userId = ""

        viewModel.getProfile(token)
        viewModel.profileResponse.observe(this) { item ->
            if (item != null) {
                Glide.with(this)
                    .load(item.avatarLink)
                    .placeholder(R.drawable.ic_user)
                    .into(binding.imageProfile)

                binding.textUsername.text = item.nickName
                binding.editName.setText(item.name)
                binding.editEmail.setText(item.email)
                binding.editAvatarLink.setText(item.avatarLink)

                val serverDateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

                dateFormat = item.birthDate
                userId = item.id

                try {
                    val birthDateFormat = serverDateFormat.parse(item.birthDate)
                    birthDate = birthDateFormat?.let { simpleDateFormat.format(it) }
                    binding.editBirthday.text = birthDate
                } catch (e: Exception) {
                    e.printStackTrace()
                    val otherServerDateFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val otherSimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val birthDateFormat = otherServerDateFormat.parse(item.birthDate)
                    birthDate = birthDateFormat?.let { otherSimpleDateFormat.format(it) }
                    binding.editBirthday.text = birthDate
                }

                if (item.gender == 1) {
                    binding.buttonMale.background = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.border_male_clicked
                    )
                    binding.buttonMale.setTextColor(resources.getColor(R.color.white))
                    binding.buttonFemale.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.border_female)
                    binding.buttonFemale.setTextColor(resources.getColor(R.color.gray_faded))
                    gender = 1
                } else if (item.gender == 2) {
                    binding.buttonFemale.background =
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.border_female_clicked
                        )
                    binding.buttonFemale.setTextColor(resources.getColor(R.color.white))

                    binding.buttonMale.background = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.border_male
                    )
                    binding.buttonMale.setTextColor(resources.getColor(R.color.gray_faded))
                    gender = 2
                }
            }
        }

        binding.buttonMale.setOnClickListener {
            binding.buttonMale.background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.border_male_clicked
            )
            binding.buttonMale.setTextColor(resources.getColor(R.color.white))
            binding.buttonFemale.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.border_female)
            binding.buttonFemale.setTextColor(resources.getColor(R.color.gray_faded))
            gender = 1
        }

        binding.buttonFemale.setOnClickListener {
            binding.buttonFemale.background =
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.border_female_clicked
                )
            binding.buttonFemale.setTextColor(resources.getColor(R.color.white))

            binding.buttonMale.background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.border_male
            )
            binding.buttonMale.setTextColor(resources.getColor(R.color.gray_faded))
            gender = 2
        }

        binding.buttonLogout.setOnClickListener {
            viewModel.logoutUser()
            tokenSharedPreferences.removeToken()
            startActivity(Intent(requireContext(), SignInActivity::class.java))
            requireActivity().finish()
        }

        binding.buttonSave.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val avatarLink = binding.editAvatarLink.text.toString()
            val name = binding.editName.text.toString()

            val userProfile = ProfileResponse(
                email = email,
                avatarLink = avatarLink,
                name = name,
                gender = gender,
                birthDate = dateFormat.toString(),
                id = userId,
                nickName = binding.textUsername.text.toString()
            )

            viewModel.editProfile(token, userProfile)
            viewModel.responseCode.observe(viewLifecycleOwner) {
                if (it == 200) {
                    Toast.makeText(
                        requireContext(),
                        "Successfully edit profile",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "Failed edit profile", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.editBirthday.setOnClickListener { showCalendar(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showCalendar(view: View) {
        val calendarSelected = Calendar.getInstance()
        if (birthDate != null) {
            val day = birthDate?.split('.')?.get(0)?.toInt()
            val month = birthDate?.split('.')?.get(1)?.toInt()
            val year = birthDate?.split('.')?.get(2)?.toInt()

            if (day != null && month != null && year != null) {
                calendarSelected.set(Calendar.DAY_OF_MONTH, day)
                calendarSelected.set(Calendar.MONTH, month - 1)
                calendarSelected.set(Calendar.YEAR, year)
            }
        }
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(calendarSelected.timeInMillis)
            .build()

        datePicker.show(parentFragmentManager, "datePicker")

        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            dateFormat = isoFormat.format(calendar.time).toString()

            binding.editBirthday.text = simpleDateFormat.format(calendar.time).toString()
            binding.editBirthday.setTextColor(resources.getColor(R.color.orange))
        }
    }
}