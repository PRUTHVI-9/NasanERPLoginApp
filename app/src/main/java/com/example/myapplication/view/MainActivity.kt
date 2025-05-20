package com.example.myapplication.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val userId = intent?.getStringExtra("emp_id") ?: "0"

        if (userId != "0") {
            viewModel.fetchEmpDetails(userId)
        }


        viewModel.result.observe(this) {
            when(it) {
                is UiState.Loading -> {

                }
                is UiState.Success -> {
                    val currentDate = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val formattedDate = currentDate.format(formatter)

                    binding.txtDate.text = formattedDate
                    val name = it.data?.data[0]?.firstName + " " + it.data?.data[0]?.lastName
                    binding.txtEmployeeName.text = name
                }
                is UiState.Error -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.btnTodayWork.setOnClickListener {
            val intent = Intent(applicationContext, RoutineWorkActivity::class.java)
            intent.putExtra("emp_id", userId)
            startActivity(intent)
        }

        binding.btnRecon.setOnClickListener {
            val intent = Intent(applicationContext, ReconActivity::class.java)
            intent.putExtra("emp_id", userId)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setPositiveButton("Yes") { dialog, _ ->
                    val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                    sharedPref.edit().clear().apply()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }


    }
}