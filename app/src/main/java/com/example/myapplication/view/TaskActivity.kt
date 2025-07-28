package com.example.myapplication.view

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.TaskAdapter
import com.example.myapplication.adapter.TaskAdapter2
import com.example.myapplication.adapter.TaskAdapter3
import com.example.myapplication.adapter.TaskAdapter4
import com.example.myapplication.data.model.TaskItem
import com.example.myapplication.databinding.ActivityTaskBinding
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.TaskViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityTaskBinding
    lateinit var viewModel: TaskViewModel
    var chipText = "Assigned To Self"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setBinding()
        setViewModel()
        setupObserver()

        enableEdgeToEdge()
//        setContentView(R.layout.activity_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // get emp id here
        val empId = intent?.getStringExtra("emp_id") ?: "0"

        // fetch tasks of employee id
        viewModel.fetchTasks(empId)

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != View.NO_ID) {
                val selectedChip = group.findViewById<Chip>(checkedId)
                chipText = selectedChip.text.toString()

                // fetch tasks of employee id
                viewModel.fetchTasks(empId)
                Toast.makeText(this, "Selected: $chipText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupObserver() {
        viewModel.result.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.txtNoRecordFound.visibility = View.GONE
                }

                is UiState.Success -> {
                    val status = it.data?.status == true
                    if (status) {
                        when (chipText) {
                            "Assigned To Self" -> {
                                var data = it.data.data?.assignedToSelfData ?: emptyList()
                                setAdapter1(data)
                            }

                            "Assigned To Others" -> {
                                val data = it.data.data?.assignedToOthersData ?: emptyList()
                                setAdapter2(data)
                            }

                            "Assigned By Others" -> {
                                val data = it.data.data?.assignedByOthersData ?: emptyList()
                                setAdapter3(data)
                            }

                            "Task Verification" -> {
                                val data = it.data.data?.taskVerificationData ?: emptyList()
                                setAdapter4(data)
                            }
                        }


                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.txtNoRecordFound.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.txtNoRecordFound.visibility = View.GONE
                }
            }
        }
    }

    private fun setAdapter4(data: List<TaskItem>) {
        val adapter = TaskAdapter4(data)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        if (data.isEmpty()) {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.txtNoRecordFound.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.txtNoRecordFound.visibility = View.GONE
        }
    }

    private fun setAdapter3(data: List<TaskItem>) {
        val adapter = TaskAdapter3(data)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        if (data.isEmpty()) {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.txtNoRecordFound.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.txtNoRecordFound.visibility = View.GONE
        }
    }

    private fun setAdapter2(data: List<TaskItem>) {
        val adapter = TaskAdapter2(data)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        if (data.isEmpty()) {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.txtNoRecordFound.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.txtNoRecordFound.visibility = View.GONE
        }
    }

    private fun setAdapter1(data: List<TaskItem>) {
        val adapter = TaskAdapter(data)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        if (data.isEmpty()) {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.txtNoRecordFound.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.txtNoRecordFound.visibility = View.GONE
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
    }

    private fun setBinding() {
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}