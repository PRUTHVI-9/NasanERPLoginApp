package com.example.myapplication.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
                    val name = it.data?.data?.get(0)?.firstName + " " + it.data?.data?.get(0)?.lastName
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

        binding.btnPendingWork.setOnClickListener {
            val intent = Intent(applicationContext, PendingRoutineActivity::class.java)
            intent.putExtra("emp_id", userId)
            startActivity(intent)
        }

        binding.btnRecon.setOnClickListener {
            val intent = Intent(applicationContext, ReconActivity::class.java)
            intent.putExtra("emp_id", userId)
            startActivity(intent)
        }

        binding.btnMeetings.setOnClickListener {
            val intent = Intent(applicationContext, MeetingActivity::class.java)
            intent.putExtra("emp_id", userId)
            startActivity(intent)
        }

        binding.btnTasks.setOnClickListener {
            val intent = Intent(applicationContext, TaskActivity::class.java)
            intent.putExtra("emp_id", userId)
            startActivity(intent)
        }

        binding.txtAddMeeting.setOnClickListener {
            val intent = Intent(applicationContext, AddMeetingActivity::class.java)
            intent.putExtra("emp_id", userId)
            println("userid$userId")
            startActivity(intent)
        }


        binding.txtOutForOfficeWork.setOnClickListener {
            val intent = Intent(applicationContext, OutForOfficeWorkActivity::class.java)
            intent.putExtra("emp_id", userId)
            println("userid$userId")
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setPositiveButton("Yes") { dialog, _ ->
                    val sharedPref = applicationContext.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                    sharedPref.edit() {clear()}

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