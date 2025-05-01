package com.hibahuns.dentassist.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.data.pref.UserPreference
import com.hibahuns.dentassist.data.pref.dataStore
import com.hibahuns.dentassist.databinding.FragmentProfileBinding
import com.hibahuns.dentassist.ui.ViewModelFactory
import com.hibahuns.dentassist.ui.login.LoginActivity

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

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val username = "Teuku Umar"
        var userEmail = "teukubrebes@gmail.com"

        profilePicture = binding.profilePicture

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.userEmail.text = user.email
            if (!user.isLogin) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                userEmail = user.email
            }
        }

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

        binding.btnSetting.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_sg,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_profile, false)
                    .build()
            )
        }

        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
        }

        binding.divProfilePicture.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        return root
    }

    fun getColorFromAttr(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as AppCompatActivity

        val actionBarColor = getColorFromAttr(activity, R.attr.colorPrimaryTool)
        val titleColor = getColorFromAttr(activity, R.attr.colorAccent)

        activity.supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(actionBarColor))
            title = Html.fromHtml("<font color='${String.format("#%06X", 0xFFFFFF and titleColor)}'>DentAssist</font>", 1)
        }
    }
}
