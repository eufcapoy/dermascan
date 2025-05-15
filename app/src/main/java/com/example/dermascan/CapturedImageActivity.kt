package com.example.dermascan

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity

class CapturedImageActivity : Activity() {

    private lateinit var capturedImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_captured_image)

        capturedImageView = findViewById(R.id.capturedImageView)

        val imageUriString = intent.getStringExtra("image_uri")
        val imageUri = Uri.parse(imageUriString)
        capturedImageView.setImageURI(imageUri)
    }
}
