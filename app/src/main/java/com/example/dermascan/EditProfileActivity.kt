package com.example.dermascan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import api.ApiService
import api.RetrofitClient
import api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val backBtn = findViewById<ImageButton>(R.id.imageButton2)
        val updateBtn = findViewById<Button>(R.id.updateBtn)
        val usernameInput = findViewById<EditText>(R.id.editUsername)
        val emailInput = findViewById<EditText>(R.id.editEmail)
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""

        // Get passed values
        val currentUsername = intent.getStringExtra("username")
        val currentEmail = intent.getStringExtra("email")

        // Set values in EditText
        usernameInput.setText(currentUsername)
        emailInput.setText(currentEmail)

        backBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("username", currentUsername)
            intent.putExtra("email", currentEmail)
            startActivity(intent)
        }

        updateBtn.setOnClickListener {
            val updatedUsername = usernameInput.text.toString()
            val updatedEmail = emailInput.text.toString()

            if (updatedUsername.length < 8) {
                Toast.makeText(this, "Username must be at least 8 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(updatedEmail).matches()) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Send update request to backend
            val updatedUser = User(updatedUsername, updatedEmail, "")
            val apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)

            val call = apiService.updateProfile(updatedUser, "Bearer $token")

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful && response.body() != null) {
                        val updatedUser = response.body()!!

                        // Save to SharedPreferences
                        with(sharedPref.edit()) {
                            putString("username", updatedUser.username)
                            putString("email", updatedUser.email)
                            apply()
                        }

                        Toast.makeText(this@EditProfileActivity, "Profile updated!", Toast.LENGTH_SHORT).show()

                        // Pass updated data to ProfileActivity
                        val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                        intent.putExtra("username", updatedUser.username)
                        intent.putExtra("email", updatedUser.email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@EditProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
