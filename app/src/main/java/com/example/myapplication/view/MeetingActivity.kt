package com.example.myapplication.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.MeetingAdapter
import com.example.myapplication.adapter.RoutineWorkAdapter
import com.example.myapplication.data.model.RoutineWorkItem
import com.example.myapplication.databinding.ActivityMeetingBinding
import com.example.myapplication.databinding.ActivityPendingRoutineBinding
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.MeetingViewModel
import com.example.myapplication.viewModels.PendingRoutineViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetingActivity : AppCompatActivity() {

    lateinit var binding:  ActivityMeetingBinding
    lateinit var viewModel: MeetingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
//        setContentView(R.layout.activity_meeting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[MeetingViewModel::class]

        val empId = intent?.getStringExtra("emp_id") ?: "0"

        if (empId != "0") {
            binding.recyclerView.visibility = View.GONE
            binding.txtNoRecordFound.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.fetchMeetingResponse(empId)
        }

        viewModel.result.observe(this) {
            when(it) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.txtNoRecordFound.visibility = View.GONE
                }
                is UiState.Success -> {
                    val data = it.data
                    if (data?.status == true) {

                        val data = it.data.data
                        val adapter = MeetingAdapter(data)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(this)

                        binding.recyclerView.visibility = View.VISIBLE
                        binding.txtNoRecordFound.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                    } else {
                        val list = ArrayList<RoutineWorkItem>()
                        list.add(
                            RoutineWorkItem(
                                "Prepare daily consumption report",
                                "Daily",
                                "19-05-2025",
                                "19-05-2025",
                                "45 min"
                            ))
                        list.add(
                            RoutineWorkItem(
                                "Approve GRN",
                                "Daily",
                                "19-05-2025",
                                "20-05-2025",
                                "35 min"
                            ))
                        list.add(
                            RoutineWorkItem(
                                "Daily Stock Report",
                                "Daily",
                                "19-05-2025",
                                "20-05-2025",
                                "40 min"
                            ))
                        val adapter = RoutineWorkAdapter(list)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(this)

                        binding.recyclerView.visibility = View.VISIBLE
                        binding.txtNoRecordFound.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                    }
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.txtNoRecordFound.visibility = View.GONE
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}