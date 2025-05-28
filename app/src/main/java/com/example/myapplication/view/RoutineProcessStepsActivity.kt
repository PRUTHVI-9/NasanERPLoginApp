package com.example.myapplication.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.StepAdapter
import com.example.myapplication.databinding.ActivityRoutineProcessStepsBinding
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.RoutineProcessStepsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutineProcessStepsActivity : AppCompatActivity() {

    lateinit var binding: ActivityRoutineProcessStepsBinding
    lateinit var viewModel: RoutineProcessStepsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoutineProcessStepsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
//        setContentView(R.layout.activity_routine_process_steps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[RoutineProcessStepsViewModel::class.java]

        val routineId = intent?.getStringExtra("routine_id") ?: "0"
        viewModel.fetchRoutineProcessSteps(routineId)

        viewModel.result.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.txtNoRecordFound.visibility = View.GONE
                }

                is UiState.Success -> {
                    val data = it.data?.data ?: emptyList()
                    val adapter = StepAdapter(data)

                    binding.recyclerView.adapter = adapter
                    binding.recyclerView.layoutManager = LinearLayoutManager(this)

                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.txtNoRecordFound.visibility = View.GONE
                }

                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.txtNoRecordFound.visibility = View.VISIBLE
                }
            }
        }
    }
}