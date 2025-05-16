package com.example.dermascan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import api.ApiService
import api.RetrofitClient
import api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val regbtn = findViewById<TextView>(R.id.textView11)
        val registerbtn = findViewById<Button>(R.id.button4)

        val usernameInput = findViewById<EditText>(R.id.editUsername)
        val emailInput = findViewById<EditText>(R.id.editEmail)
        val passwordInput = findViewById<EditText>(R.id.editPassword)
        val confirmPasswordInput = findViewById<EditText>(R.id.editconfirmPassword)



        regbtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        registerbtn.setOnClickListener {
            val username = usernameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()
            val user = User(username, email, password)
            val apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)
            val call = apiService.register(user)
            val emailPattern = android.util.Patterns.EMAIL_ADDRESS
            when {
                username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
                username.length < 8 -> {
                    Toast.makeText(this, "Username must be at least 8 characters", Toast.LENGTH_SHORT).show()
                }
                !emailPattern.matcher(email).matches() -> {
                    Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                }
                password.length < 8 -> {
                    Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // All checks passed, proceed with API call
                    val user = User(username, email, password)
                    val apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)
                    val call = apiService.register(user)

                    call.enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@RegisterActivity, "Registered successfully!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}
