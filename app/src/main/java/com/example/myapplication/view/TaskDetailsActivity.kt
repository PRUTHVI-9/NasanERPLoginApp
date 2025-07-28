package com.example.myapplication.view;

import android.graphics.Color
import android.os.Bundle;
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityTaskDetailsBinding
import com.example.myapplication.databinding.WorkDetailsBinding
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.TaskDetailsViewModel
import com.example.myapplication.viewModels.WorkDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class TaskDetailsActivity :  AppCompatActivity() {

    lateinit var viewModel: TaskDetailsViewModel
    lateinit var binding: ActivityTaskDetailsBinding
    private var timerJob: Job? = null
    var taskId = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupViewModel()
        setupObserver()



        taskId = intent.getStringExtra("task_id") ?: "0"
        binding.title.text = intent.getStringExtra("task_name")
        val timeReq = intent.getStringExtra("time_req") ?: "0"



        if (taskId != "0")
            viewModel.fetchTaskStatus(taskId)

        binding.btnStart.setOnClickListener {
            viewModel.doTaskOperation(taskId, "started", timeReq)
        }

        binding.btnPause.setOnClickListener {
            viewModel.doTaskOperation(taskId, "paused", timeReq)
            stopTimer()
        }

        binding.btnStop.setOnClickListener {
            viewModel.doTaskOperation(taskId, "stopped", timeReq)
            stopTimer()
        }

    }

    private fun parseStartTimeToMillis(timeString: String): Long {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault() // or use UTC if the server sends in UTC
        return formatter.parse(timeString)?.time ?: System.currentTimeMillis()
    }


    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun startTimer(startFromMillis: Long, resumeFromElapsed: Long = 0L) {
        timerJob?.cancel()

        // Adjust base start time for previously elapsed duration
        val baseStartTime = startFromMillis - resumeFromElapsed

        timerJob = lifecycleScope.launch {
            while (isActive) {
                val elapsedMillis = System.currentTimeMillis() - baseStartTime

                val seconds = (elapsedMillis / 1000) % 60
                val minutes = (elapsedMillis / (1000 * 60)) % 60
                val hours = (elapsedMillis / (1000 * 60 * 60))

                val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.txtStartTime.text = timeFormatted

                delay(1000)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[TaskDetailsViewModel::class.java]
    }

    private fun setupBinding() {
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupObserver() {
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
//                            binding.btnBackToHomePage.visibility = View.GONE
                        }

                        "paused" -> {
                            binding.btnStart.setBackgroundColor(Color.GRAY)
                            binding.btnPause.setBackgroundColor(Color.RED)
                            binding.btnStop.setBackgroundColor(Color.GRAY)
//                            binding.btnBackToHomePage.visibility = View.VISIBLE
                        }

                        "stopped" -> {
                            binding.btnStart.setBackgroundColor(Color.GRAY)
                            binding.btnPause.setBackgroundColor(Color.GRAY)
                            binding.btnStop.setBackgroundColor(Color.RED)
//                            binding.btnBackToHomePage.visibility = View.GONE
                        }

                        /* "submit" ->{
                             binding.subbutton.setBackgroundColor(Color.GRAY)  //added
                         }*/
                    }
                    viewModel.fetchTaskStatus(taskId)
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

//                                val startTimeString = it.data.data[0].startTime // "2025-06-16 14:30:00"
//                                startTimeMillis = parseStartTimeToMillis(startTimeString)
//                                startTimer(startTimeMillis)

                                val apiStartTime = it.data.data[0].startTime
                                val startTimeMillis = parseStartTimeToMillis(apiStartTime)
                                val elapsedNow = System.currentTimeMillis() - startTimeMillis
                                startTimer(startTimeMillis, elapsedNow)
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
    }

}