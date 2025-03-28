package com.hibahuns.dentassist.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.databinding.FragmentProfileBinding
import com.hibahuns.dentassist.ui.ViewModelFactory
import com.hibahuns.dentassist.ui.login.LoginActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        binding.btnEditProfile.setOnClickListener {
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
