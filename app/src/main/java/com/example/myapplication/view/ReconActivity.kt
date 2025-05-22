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
import com.example.myapplication.adapter.RoutineWorkAdapter
import com.example.myapplication.data.model.RoutineWorkItem
import com.example.myapplication.databinding.ActivityReconBinding
import com.example.myapplication.databinding.ActivityRoutineWorkBinding
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.ReconViewModel
import com.example.myapplication.viewModels.RoutineWorkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReconActivity : AppCompatActivity() {

    lateinit var binding:  ActivityReconBinding
    lateinit var viewModel: ReconViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityReconBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
//        setContentView(R.layout.activity_recon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[ReconViewModel::class]

        val empId = intent?.getStringExtra("emp_id") ?: "0"
        if (empId != "0") {
            binding.recyclerView.visibility = View.GONE
            binding.txtNoRecordFound.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.fetchRecon(empId)
        }

        viewModel.result.observe(this) {
            when(it) {
                is UiState.Loading -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.txtNoRecordFound.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    if (it.data?.status == true) {
                        val data = it.data?.data ?: emptyList()
                        val adapter = RoutineWorkAdapter(data)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(this)

                        binding.recyclerView.visibility = View.VISIBLE
                        binding.txtNoRecordFound.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                    } else {
                        binding.recyclerView.visibility = View.GONE
                        binding.txtNoRecordFound.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }
                }
                is UiState.Error -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.txtNoRecordFound.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}