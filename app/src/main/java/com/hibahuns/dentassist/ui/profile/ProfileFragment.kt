package com.hibahuns.dentassist.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.data.pref.UserPreference
import com.hibahuns.dentassist.data.pref.dataStore
import com.hibahuns.dentassist.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profilePicture: ImageView
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                profilePicture.setImageURI(it)

//            start
                val file = File(it.path!!)
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

                val userPreference = UserPreference.getInstance(requireContext().dataStore)
                lifecycleScope.launch {
                    userPreference.getSession().collect { userModel ->
//                    val idUserRequestBody = userModel.idUser.toRequestBody("text/plain".toMediaTypeOrNull())
                        val json = "SDzHFAoNiHtFBnHE58EJ"
                        val idUserRequestBody = json.toRequestBody("text/plain".toMediaType())

//                        dashboardViewModel.predict(imageBody, idUserRequestBody)
                    }
                }
//            end
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profilePicture = binding.profilePicture

        val username = "Teuku Umar"
        val userEmail = "teukubrebes123@gmail.com"

        binding.username.text = username
        binding.userEmail.text = userEmail

        binding.btnCs.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_cs,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_profile, false)
                    .build()
            )
        }

        binding.divProfilePicture.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
            title = Html.fromHtml("<font color='#EA7676'>Profile</font>", 1)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
        }
    }

}