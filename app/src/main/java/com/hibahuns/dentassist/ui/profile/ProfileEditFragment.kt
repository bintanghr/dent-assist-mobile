package com.hibahuns.dentassist.ui.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.bumptech.glide.Glide
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.data.pref.UserPreference
import com.hibahuns.dentassist.data.pref.dataStore
import com.hibahuns.dentassist.databinding.FragmentProfileEditBinding
import com.hibahuns.dentassist.ui.ViewModelFactory
import com.hibahuns.dentassist.ui.home.HomeViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileEditFragment : Fragment() {
    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!
    private var imageBody: MultipartBody.Part? = null

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireContext())
        )[ProfileViewModel::class.java]
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.profilePicture.setImageURI(it)
                val contentResolver = requireContext().contentResolver
                val inputStream = contentResolver.openInputStream(it)
                val fileName = requireContext().getFileName(it)

                val tempFile = File.createTempFile("upload", fileName.substringAfterLast("."))
                tempFile.outputStream().use { output ->
                    inputStream?.copyTo(output)
                }

                val requestFile = tempFile
                    .asRequestBody("image/*".toMediaTypeOrNull())
                imageBody = MultipartBody.Part.createFormData("profileImage", tempFile.name, requestFile)
            }
        }

    private fun Context.getFileName(uri: Uri): String {
        var name = "file"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
        setupView()
        setupAction()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    private fun fetchData() {
        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(requireContext().dataStore)
            userPreference.getSession().collect { userModel ->
                val userId = userModel.idUser

                profileViewModel.fetchData(userId)
            }
        }

        profileViewModel.userData.observe(viewLifecycleOwner) { userResponse ->
            val user = userResponse?.data
            binding.nameEditText.text = Editable.Factory.getInstance().newEditable(user?.username)
            binding.kotaEditText.text = Editable.Factory.getInstance().newEditable(user?.city)
            binding.emailEditText.text = Editable.Factory.getInstance().newEditable(user?.email)
            binding.passwordEditText.text = Editable.Factory.getInstance().newEditable(user?.password)
            Glide.with(this)
                .load(user?.profileImage)
                .placeholder(R.drawable.image_preview)
                .error(R.drawable.image_preview)
                .into(binding.profilePicture)
        }
    }

    private fun setupView() {
        binding.nameEditText.addTextChangedListener(textWatcher)
        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.kotaEditText.addTextChangedListener(textWatcher)

        binding.passwordEditTextLayout.visibility = View.GONE
        binding.tPassword.visibility = View.GONE
    }

    private fun setupAction() {
        binding.divProfilePicture.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.signupButton.setOnClickListener {
            val username = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val city = binding.kotaEditText.text.toString().trim()
//            val profileImage = binding.

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || city.isEmpty()) {
                Toast.makeText(context, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

//            if (password.length < 8 || !password.matches(Regex(".*[A-Za-z].*")) || !password.matches(Regex(".*\\d.*"))) {
//                Toast.makeText(
//                    context,
//                    "Password harus minimal 8 karakter, mengandung huruf, dan angka!",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//            }

            lifecycleScope.launch {
                val usernameRequestBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailRequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val passwordRequestBody = null
                val cityRequestBody = city.toRequestBody("text/plain".toMediaTypeOrNull())

                val userPreference = UserPreference.getInstance(requireContext().dataStore)
                userPreference.getSession().collect { userModel ->
                    val userId = userModel.idUser

                    profileViewModel.updateUserProfile(
                        userId,
                        usernameRequestBody,
                        emailRequestBody,
                        passwordRequestBody,
                        cityRequestBody,
                        imageBody
                    )
                }
            }
            fetchData()
            findNavController(this).popBackStack()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

            val isUsernameFilled = binding.nameEditText.text?.isNotEmpty() ?: false
            val isCityFilled = binding.passwordEditText.text?.isNotEmpty() ?: false
            val isEmailFilled = binding.emailEditText.text?.isNotEmpty() ?: false

            if (isEmailFilled && isCityFilled && isUsernameFilled) {
                binding.signupButton.background = ContextCompat.getDrawable(binding.root.context, R.drawable.button2)
            } else {
                binding.signupButton.background = ContextCompat.getDrawable(binding.root.context, R.drawable.button2_disabled)
            }
        }

        override fun afterTextChanged(charSequence: Editable?) {}
    }
}