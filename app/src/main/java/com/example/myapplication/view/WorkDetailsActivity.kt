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
import androidx.lifecycle.lifecycleScope
import androidx.transition.Visibility
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityWorkDetailsBinding
import com.example.myapplication.databinding.WorkDetailsBinding
import com.example.myapplication.utils.UiState
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
class WorkDetailsActivity : AppCompatActivity() {

    private var startTimeMillis: Long = 0L
    private var timerJob: Job? = null
    private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())


    //    lateinit var binding: ActivityWorkDetailsBinding
    lateinit var binding: WorkDetailsBinding
    lateinit var viewModel: WorkDetailsViewModel

    var list = ArrayList<String>()
    var selectedReason = ""

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
            stopTimer()
        }

        binding.btnStop.setOnClickListener {
            viewModel.doActionOnRoutine(routineId, routineDate, "stopped", timeReq)
            stopTimer()
        }

        binding.viewSystem.setOnClickListener {
            val intent = Intent(applicationContext, RoutineProcessStepsActivity::class.java)
            intent.putExtra("routine_id", routineId)
            startActivity(intent)
        }

        binding.edtReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedReason = parent.getItemAtPosition(position).toString()


//                val index = list.indexOf(selectedItem)


//                Toast.makeText(
//                    this@WorkDetailsActivity,
//                    "Selected: $selectedItem",
//                    Toast.LENGTH_SHORT
//                ).show()

                if (position != 0) {
                    binding.btnSkip.isEnabled = true

                } else {
                    binding.btnSkip.isEnabled = false
                }
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onNothingSelected(parent: AdapterView<*>) {
                binding.btnSkip.isEnabled = false
            }
        }

        binding.btnSkip.setOnClickListener() {
            viewModel.skipReason(routineId, routineDate, selectedReason)
        }

        binding.btnBackToHomePage.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        viewModel.resultSkipReason.observe(this) {
            when (it) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    Toast.makeText(applicationContext, "${it.data?.message}", Toast.LENGTH_SHORT).show()

                    if (it.data?.status == true) finish()

                }

                is UiState.Error -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
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
                            binding.btnBackToHomePage.visibility = View.GONE
                        }

                        "paused" -> {
                            binding.btnStart.setBackgroundColor(Color.GRAY)
                            binding.btnPause.setBackgroundColor(Color.RED)
                            binding.btnStop.setBackgroundColor(Color.GRAY)
                            binding.btnBackToHomePage.visibility = View.VISIBLE
                        }

                        "stopped" -> {
                            binding.btnStart.setBackgroundColor(Color.GRAY)
                            binding.btnPause.setBackgroundColor(Color.GRAY)
                            binding.btnStop.setBackgroundColor(Color.RED)
                            binding.btnBackToHomePage.visibility = View.GONE
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

        viewModel.resultReasons.observe(this) {
            when (it) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {


                    val data = it.data?.data ?: emptyList()
                    list.clear()
                    list.add("SELECT REASON")
                    for (item in data) {
                        list.add(item.reasonName)
                    }
                    // Create adapter
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.edtReason.adapter = adapter
                }

                is UiState.Error -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
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



    private fun startTimer(fromTimeMillis: Long) {
        timerJob?.cancel()
        timerJob = lifecycleScope.launch {
            while (isActive) {
                val elapsedMillis = System.currentTimeMillis() - fromTimeMillis
                val seconds = (elapsedMillis / 1000) % 60
                val minutes = (elapsedMillis / (1000 * 60)) % 60
                val hours = (elapsedMillis / (1000 * 60 * 60))
                val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.txtStartTime.text = time
                delay(1000)
            }
        }
    }

}