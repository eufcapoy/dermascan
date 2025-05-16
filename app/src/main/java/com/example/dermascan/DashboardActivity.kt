package com.example.dermascan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.Manifest
import android.content.pm.PackageManager
import android.widget.TextView
import api.ApiService
import api.RetrofitClient
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File

class DashboardActivity : AppCompatActivity() {
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null
    private var isDrawerOpen = false

    private val CAMERA_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val overlay = findViewById<View>(R.id.overlay)
        val customNavDrawer = findViewById<ConstraintLayout>(R.id.customNavDrawer)
        val imageButton = findViewById<ImageView>(R.id.imageView2)
        val profileBtn = findViewById<ImageView>(R.id.imageView3)
        val navprofileBtn = findViewById<LinearLayout>(R.id.profile)
        val navsubscriptionBtn = findViewById<LinearLayout>(R.id.subscription)
        val logoutBtn = findViewById<LinearLayout>(R.id.logoutButton)
        val historyBtn = findViewById<LinearLayout>(R.id.nav_history)
        val cameraButton = findViewById<FrameLayout>(R.id.cameraButton)
        val customName = findViewById<TextView>(R.id.textView12)
        val username = intent.getStringExtra("username") ?: "User"
        val email = intent.getStringExtra("email") ?: "Email"
        customName.text = "Hello, $username!"

        // Camera launcher setup
        /* cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri != null) {
                val intent = Intent(this, CapturedImageActivity::class.java).apply {
                    putExtra("image_uri", imageUri.toString())
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
            val file = File(imageUri?.path ?: return@registerForActivityResult)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val api = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)
            val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            val token = sharedPref.getString("token", "") ?: ""

            api.uploadScan(body, "Bearer $token").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DashboardActivity, "Scan uploaded", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DashboardActivity, "Upload failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@DashboardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } */

        cameraButton.setOnClickListener {
            // Check camera permission before opening the camera
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }

        imageButton.setOnClickListener {
            if (isDrawerOpen) {
                customNavDrawer.translationX = -280f
                customNavDrawer.visibility = View.GONE
                overlay.visibility = View.GONE
            } else {
                customNavDrawer.visibility = View.VISIBLE
                overlay.visibility = View.VISIBLE
                customNavDrawer.translationX = 0f
            }
            isDrawerOpen = !isDrawerOpen
        }

        overlay.setOnClickListener {
            customNavDrawer.translationX = -280f
            customNavDrawer.visibility = View.GONE
            overlay.visibility = View.GONE
            isDrawerOpen = false
        }

        profileBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("username", username);
            intent.putExtra("email", email);
            startActivity(intent)
            finish()
        }

        navprofileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        navsubscriptionBtn.setOnClickListener {
            startActivity(Intent(this, SubcriptionActivity::class.java))
            finish()
        }

        logoutBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        historyBtn.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
            finish()
        }
    }

    private fun openCamera() {
        val photoFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo_${System.currentTimeMillis()}.jpg")
        imageUri = FileProvider.getUriForFile(this, "${packageName}.provider", photoFile)
        cameraLauncher.launch(imageUri)
    }

    // Handle the permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, now open the camera
                openCamera()
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
