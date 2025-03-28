package com.hibahuns.dentassist.ui.signup

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hibahuns.dentassist.databinding.ActivitySignupBinding
import com.hibahuns.dentassist.ui.login.LoginActivity
import com.hibahuns.dentassist.ui.ViewModelFactory
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hibahuns.dentassist.R

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val signupViewModel: SignupViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        observeViewModel()
        setupListeners()
    }
    private fun setupListeners() {
        binding.punyaAkun.setOnClickListener {
            finish()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val username = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val city = binding.kotaEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || city.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 8 || !password.matches(Regex(".*[A-Za-z].*")) || !password.matches(Regex(".*\\d.*"))) {
                Toast.makeText(
                    this,
                    "Password harus minimal 8 karakter, mengandung huruf, dan angka!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            signupViewModel.signup(username, email, password, city)
        }
    }


    private fun observeViewModel() {

        fun getColorFromAttr(context: Context, attr: Int): Int {
            val typedValue = TypedValue()
            val theme = context.theme
            theme.resolveAttribute(attr, typedValue, true)
            return typedValue.data
        }

        signupViewModel.signupResult.observe(this) { response ->
            if (response != null) {
                val textColor = getColorFromAttr(this, R.attr.colorDialogText)
                val buttonColor = getColorFromAttr(this, R.attr.colorDialogButton)
                val blackColor = ContextCompat.getColor(this, android.R.color.black)

                val title = SpannableString("Yeah!").apply {
                    setSpan(ForegroundColorSpan(blackColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                val message = SpannableString(response.message ?: "Pendaftaran berhasil! Silahkan login.").apply {
                    setSpan(ForegroundColorSpan(blackColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                val dialog = AlertDialog.Builder(this).apply {
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("Lanjut") { _, _ ->
                        finish()
                    }
                }.create()

                dialog.setOnShowListener {
                    dialog.findViewById<TextView>(android.R.id.message)?.setTextColor(textColor)
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(buttonColor)
                }

                dialog.show()
            }
        }


        signupViewModel.errorMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        signupViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}