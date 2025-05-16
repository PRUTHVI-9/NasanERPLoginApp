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
import com.example.myapplication.adapter.RoutineWorkAdapter
import com.example.myapplication.databinding.ActivityRoutineWorkBinding
import com.example.myapplication.viewModels.RoutineWorkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutineWorkActivity : AppCompatActivity() {

    lateinit var binding:  ActivityRoutineWorkBinding
    lateinit var viewModel: RoutineWorkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRoutineWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
//        setContentView(R.layout.activity_routine_work)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        viewModel = ViewModelProvider(this)[RoutineWorkViewModel::class]

        val empId = intent?.getStringExtra("emp_id") ?: "0"
        if (empId != "0") {
            binding.recyclerView.visibility = View.GONE
            binding.txtNoRecordFound.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.fetchRoutineWork(empId)
        }

        viewModel.result.observe(this) {
            if (it.status) {
                val data = it.data
                val adapter = RoutineWorkAdapter(data)

                binding.recyclerView.visibility = View.VISIBLE
                binding.txtNoRecordFound.visibility = View.GONE
                binding.progressBar.visibility = View.GONE

                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.txtNoRecordFound.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}