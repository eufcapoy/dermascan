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
import api.LoginRequest
import api.LoginResponse
import api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val regbtn = findViewById<TextView>(R.id.textView6)
        val loginbtn = findViewById<Button>(R.id.button3)
        val forgotpassbtn = findViewById<TextView>(R.id.textView7)

        val emailInput = findViewById<EditText>(R.id.editLoginEmail)
        val passwordInput = findViewById<EditText>(R.id.editLoginPassword)


        regbtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        forgotpassbtn.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }

        loginbtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            val emailPattern = android.util.Patterns.EMAIL_ADDRESS


            when {
                email.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
                !emailPattern.matcher(email).matches() -> {
                    Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                }
                password.length < 8 -> {
                    Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val loginRequest = LoginRequest(email, password)
                    val apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)
                    val call = apiService.login(loginRequest)
                    call.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            if (response.isSuccessful) {

                                val loginResponse = response.body()
                                val user = loginResponse?.user
                                val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putString("token", loginResponse?.token)
                                    putString("username", loginResponse?.user?.username)
                                    putString("email", loginResponse?.user?.email)
                                    apply()
                                }
                                Log.d("LoginSuccess", "Message: " + loginResponse?.getMessage());
                                Log.d("LoginSuccess", "Username: " + loginResponse?.getUser()?.getUsername());
                                Log.d("LoginSuccess", "Email: " + loginResponse?.getUser()?.getEmail());
                                Log.d("LoginSuccess", "Token: " + loginResponse?.getToken());
                                Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()

                                val username = user?.username ?: "User"
                                val email = user?.email ?: "Email"
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                intent.putExtra("username", username)
                                intent.putExtra("email", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.e("LoginDebug", "Failed response code: ${response.code()}")
                                Log.e("LoginDebug", "Error body: ${response.errorBody()?.string()}")
                                Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Log.e("Login", "Login error: ${t.message}")
                            Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}
