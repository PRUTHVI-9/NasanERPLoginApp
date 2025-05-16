package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.viewModels.LoginViewModel
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

                val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("isLoggedIn", true)
                    putString("empId", empId)
                    apply()
                }

                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra("emp_id", empId)
                startActivity(intent)
                finish()
            }


        }
    }
}
