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
import com.example.myapplication.data.model.RoutineWorkItem
import com.example.myapplication.databinding.ActivityReconBinding
import com.example.myapplication.databinding.ActivityRoutineWorkBinding
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
            if (it.status) {
                val data = it.data
                val adapter = RoutineWorkAdapter(data)

                binding.recyclerView.visibility = View.VISIBLE
                binding.txtNoRecordFound.visibility = View.GONE
                binding.progressBar.visibility = View.GONE

                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
            } else {
                val list = ArrayList<RoutineWorkItem>()
                list.add(
                    RoutineWorkItem(
                        "Prepare daily consumption report",
                        "Daily",
                        "15-05-2025",
                        "15-05-2025",
                        "45 hours"
                    ))
                list.add(
                    RoutineWorkItem(
                        "Approve GRN",
                        "Daily",
                        "15-05-2025",
                        "16-05-2025",
                        "35 hours"
                    ))
                list.add(
                    RoutineWorkItem(
                        "Daily Stock Report",
                        "Daily",
                        "15-05-2025",
                        "16-05-2025",
                        "40 hours"
                    ))
                val adapter = RoutineWorkAdapter(list)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this)

                /*binding.recyclerView.visibility = View.GONE
                binding.txtNoRecordFound.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE*/
                binding.recyclerView.visibility = View.VISIBLE
                binding.txtNoRecordFound.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}