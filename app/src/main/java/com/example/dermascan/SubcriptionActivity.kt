package com.example.dermascan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton

class SubcriptionActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subcription)

        val backBtn = findViewById<ImageButton>(R.id.imageButton3)
        val currentUsername = intent.getStringExtra("username") ?: ""
        val currentEmail = intent.getStringExtra("email") ?: ""
        backBtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("username", currentUsername)
            intent.putExtra("email", currentEmail)
            startActivity(intent)
        }
    }
}