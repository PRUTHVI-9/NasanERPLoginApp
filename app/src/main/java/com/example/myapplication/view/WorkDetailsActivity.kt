package com.example.myapplication.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityWorkDetailsBinding
import com.example.myapplication.databinding.WorkDetailsBinding
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.WorkDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkDetailsActivity : AppCompatActivity() {

    //    lateinit var binding: ActivityWorkDetailsBinding
    lateinit var binding: WorkDetailsBinding
    lateinit var viewModel: WorkDetailsViewModel

    var list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WorkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val routineId = intent.getStringExtra("routine_id") ?: "0"
        val routineDate = intent.getStringExtra("routine_date") ?: "0"
        binding.title.text = intent.getStringExtra("routine_name")
        val timeReq = intent.getStringExtra("time_req") ?: "0"


//        val items = listOf("Enter Reason....",
//            "Due to a pending important task, I am skipping the current one.",
//            "Due to some reasons my skip the current task",
//            "Tasks are assigned by priority, but I skip a few.")  //added

        // Create adapter
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

//        binding.edtReson.adapter = adapter //rk

        viewModel = ViewModelProvider(this)[WorkDetailsViewModel::class.java]

        if (routineId != "0" && routineDate != "0")
            viewModel.fetchRoutineStatus(routineId, routineDate)

        viewModel.fetchReasons()

        binding.btnStart.setOnClickListener {
            viewModel.doActionOnRoutine(routineId, routineDate, "started", timeReq)
        }

        binding.btnPause.setOnClickListener {
            viewModel.doActionOnRoutine(routineId, routineDate, "paused", timeReq)
        }

        binding.btnStop.setOnClickListener {
            viewModel.doActionOnRoutine(routineId, routineDate, "stopped", timeReq)
        }

        binding.viewSystem.setOnClickListener {
            val intent = Intent(applicationContext, RoutineProcessStepsActivity::class.java)
            intent.putExtra("routine_id", routineId)
            startActivity(intent)
        }

        binding.edtReson.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                val id = list.indexOf(selectedItem)

                Toast.makeText(
                    this@WorkDetailsActivity,
                    "Selected: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()

                if (position != 0) {
                    binding.subbutton.isEnabled = true
                    binding.subbutton.setBackgroundColor(Color.BLUE)

                } else {
                    binding.subbutton.isEnabled = false
                    binding.subbutton.setBackgroundColor(Color.GRAY)
                }
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onNothingSelected(parent: AdapterView<*>) {
                binding.subbutton.isEnabled = false
                binding.subbutton.setBackgroundColor(Color.GRAY)
            }
        }

        binding.subbutton.setOnClickListener() {
            val intent = Intent(applicationContext, RoutineProcessStepsActivity::class.java)
            startActivity(intent)
        }

        viewModel.result.observe(this) {
            when (it) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {
                    val date = it.data?.message
                    Toast.makeText(applicationContext, date, Toast.LENGTH_SHORT).show()
                    val action = it.data?.action
                    when (action) {
                        "started" -> {
                            binding.btnStart.setBackgroundColor(Color.GREEN)
                            binding.btnPause.setBackgroundColor(Color.GRAY)
                            binding.btnStop.setBackgroundColor(Color.GRAY)
                        }

                        "paused" -> {
                            binding.btnStart.setBackgroundColor(Color.GRAY)
                            binding.btnPause.setBackgroundColor(Color.RED)
                            binding.btnStop.setBackgroundColor(Color.GRAY)
                        }

                        "stopped" -> {
                            binding.btnStart.setBackgroundColor(Color.GRAY)
                            binding.btnPause.setBackgroundColor(Color.GRAY)
                            binding.btnStop.setBackgroundColor(Color.RED)
                        }

                        /* "submit" ->{
                             binding.subbutton.setBackgroundColor(Color.GRAY)  //added
                         }*/
                    }
                    viewModel.fetchRoutineStatus(routineId, routineDate)
                }

                is UiState.Error -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.resultStatus.observe(this) {
            when (it) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    val date = it.data?.message
//                    Toast.makeText(applicationContext, date, Toast.LENGTH_SHORT).show()

                    if (it.data?.status == true) {

                        val action = it.data.data[0].status
                        val item = it.data.data[0]

                        binding.txtStartTime.text = item?.startTime ?: ""
                        binding.txtEndTime.text = item?.endTime ?: ""
                        binding.txtDuration.text =
                            "Spent: ${item?.total} mins, Total: ${item?.required} mins"

                        when (action) {
                            "started" -> {
                                binding.btnStart.setBackgroundColor(Color.GREEN)
                                binding.btnPause.setBackgroundColor(Color.GRAY)
                                binding.btnStop.setBackgroundColor(Color.GRAY)
                            }

                            "paused" -> {
                                binding.btnStart.setBackgroundColor(Color.GRAY)
                                binding.btnPause.setBackgroundColor(Color.RED)
                                binding.btnStop.setBackgroundColor(Color.GRAY)
                            }

                            "stopped" -> {
                                binding.btnStart.setBackgroundColor(Color.GRAY)
                                binding.btnPause.setBackgroundColor(Color.GRAY)
                                binding.btnStop.setBackgroundColor(Color.RED)
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.resultReasons.observe(this) {
            when (it) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {


                    val data = it.data?.data ?: emptyList()
                    list.clear()
                    for (item in data) {
                        list.add(item.reasonName)
                    }
                    // Create adapter
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.edtReson.adapter = adapter
                }

                is UiState.Error -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}