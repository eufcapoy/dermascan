
package com.example.dermascan
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import api.ApiService
import api.ChangePasswordRequest
import api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val currentPassword = findViewById<EditText>(R.id.editCurrentPassword)
        val newPassword = findViewById<EditText>(R.id.editNewPassword)
        val confirmPassword = findViewById<EditText>(R.id.editConfirmNewPassword)
        val updateBtn = findViewById<Button>(R.id.updateBtn)

        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""

        updateBtn.setOnClickListener {
            val current = currentPassword.text.toString()
            val newPass = newPassword.text.toString()
            val confirm = confirmPassword.text.toString()

            if (newPass != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPass.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = ChangePasswordRequest(current, newPass)
            val api = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)
            api.changePassword(request, "Bearer $token")
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.d("ChangePassword", "Code: ${response.code()}")
                        Log.d("ChangePassword", "Error body: ${response.errorBody()?.string()}")
                        if (response.isSuccessful) {
                            Toast.makeText(this@ChangePasswordActivity, "Password updated!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@ChangePasswordActivity, "Update failed. Check current password.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@ChangePasswordActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
