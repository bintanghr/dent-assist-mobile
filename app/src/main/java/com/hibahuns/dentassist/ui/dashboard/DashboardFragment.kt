package com.hibahuns.dentassist.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hibahuns.dentassist.data.pref.dataStore
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.data.pref.UserPreference
import com.hibahuns.dentassist.databinding.FragmentDashboardBinding
import com.hibahuns.dentassist.ui.ViewModelFactory
import java.io.File
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var previewView: PreviewView

    private var imageCapture: ImageCapture? = null
    private var currentImageUri: Uri? = null
    private var croppedImageUri: Uri? = null
    private var selectedCamera = "front_camera"

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Akses diijinkan", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Akses ditolak", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dashboardViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[DashboardViewModel::class.java]

        dashboardViewModel.predictResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val bundle = Bundle().apply {
                    putParcelable("predictionData", response.data)
                }
                view?.post {
                    findNavController().navigate(R.id.action_fragmentDashboard_to_fragmentPrediction, bundle,
                        NavOptions.Builder()
                            .setRestoreState(true)
                            .setPopUpTo(R.id.mobile_navigation, false)
                            .build()
                    )
                }
            }
            result.onFailure { error ->
                Toast.makeText(requireContext(), "Upload gagal: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startCamera(selectedCamera)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = binding.previewView
        binding.captureButton.setOnClickListener {
            takePhoto()
        }
        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.flipCameraButton.setOnClickListener {
            selectedCamera = if (selectedCamera == "front_camera") "rear_camera" else "front_camera"
            startCamera(selectedCamera)
        }
    }

    private fun startCamera(selectedCamera: String = "front_camera") {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = when (selectedCamera) {
                "front_camera" -> {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                }
                "rear_camera" -> {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
                else -> {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                }
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(requireContext(), "Gagal membuka kamera", Toast.LENGTH_SHORT).show()
                Log.e("CameraFragment", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
//            currentImageUri = uri
            startUCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun takePhoto() {
        val photoFile = File(requireContext().externalCacheDir, "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    currentImageUri = savedUri
                    startUCrop(savedUri)
                    Toast.makeText(requireContext(), "Berhasil mengambil gambar", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
                    Toast.makeText(requireContext(), "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun startUCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image.jpg"))

        val options = UCrop.Options().apply {
            setAllowedGestures(UCropActivity.SCALE, UCropActivity.NONE, UCropActivity.SCALE)
            setToolbarTitle("Crop Image")
            setFreeStyleCropEnabled(true)
            setToolbarColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            setStatusBarColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            setActiveControlsWidgetColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        }

        val intent = UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .getIntent(requireContext())

        uCropLauncher.launch(intent)
    }

    private val uCropLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            croppedImageUri = resultUri

            uploadImage()
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            cropError?.printStackTrace()
        }
    }

    private fun uploadImage() {
        if (croppedImageUri != null) {
            val file = File(croppedImageUri!!.path!!)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val userPreference = UserPreference.getInstance(requireContext().dataStore)
            lifecycleScope.launch {
                userPreference.getSession().collect { userModel ->
//                    val idUserRequestBody = userModel.idUser.toRequestBody("text/plain".toMediaTypeOrNull())
                    val json = "SDzHFAoNiHtFBnHE58EJ"
                    val idUserRequestBody = json.toRequestBody("text/plain".toMediaType())

                    dashboardViewModel.predict(imageBody, idUserRequestBody)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
            title = Html.fromHtml("<font color='#EA7676'>DentAssist</font>", 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}