package com.example.dermascan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class StartingPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_page)
        val loginbtn = findViewById<Button>(R.id.button)
        val signupbtn = findViewById<Button>(R.id.button2)
        loginbtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        signupbtn.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}