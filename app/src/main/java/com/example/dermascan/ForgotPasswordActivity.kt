package com.example.dermascan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class ForgotPasswordActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val backBtn = findViewById<ImageView>(R.id.imageView1)
        backBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val resetBtn = findViewById<Button>(R.id.button7)
        resetBtn.setOnClickListener {
            val intent = Intent(this, SendingCodeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}