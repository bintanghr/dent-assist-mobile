package com.hibahuns.dentassist.ui.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hibahuns.dentassist.MainActivity
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.databinding.ActivityLoginBinding
import com.hibahuns.dentassist.ui.ViewModelFactory
import com.hibahuns.dentassist.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        binding.daftarButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        fun getColorFromAttr(context: Context, attr: Int): Int {
            val typedValue = TypedValue()
            val theme = context.theme
            theme.resolveAttribute(attr, typedValue, true)
            return typedValue.data
        }

        viewModel.loginResult.observe(this) { response ->
            if (response != null) {
                val textColor = getColorFromAttr(this, R.attr.colorDialogText)
                val buttonColor = getColorFromAttr(this, R.attr.colorDialogButton)
                val blackColor = ContextCompat.getColor(this, android.R.color.black)

                val title = SpannableString("Yeah!").apply {
                    setSpan(ForegroundColorSpan(blackColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                val message = SpannableString(response.message ?: "Login berhasil!").apply {
                    setSpan(ForegroundColorSpan(blackColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                val dialog = AlertDialog.Builder(this).apply {
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
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
        viewModel.errorMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

}