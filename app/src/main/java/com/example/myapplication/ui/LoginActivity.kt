package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel

    var empId = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Already logged in, skip login screen
            val empId = sharedPref.getString("empId", "0") ?: "0"
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("emp_id", empId)
            startActivity(intent)
            finish()
            return
        }

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val userId = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(userId, password)
            }
        }

        viewModel.result.observe(this) {

            Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()

            if (it.status) {
                empId = it.empId

                // Save login state
                val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("isLoggedIn", true)
                    putString("empId", empId)
                    apply()
                }

                // Go to MainActivity
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra("emp_id", empId)
                startActivity(intent)
                finish() // Finish login activity so it can't be returned to
            }


        }
    }
}
