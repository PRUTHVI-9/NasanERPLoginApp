package com.example.myapplication.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.model.EmployeeList
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.EmployeeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OutForOfficeWorkActivity : AppCompatActivity() {

    private lateinit var spinnerReportingPerson: Spinner
    private lateinit var employeeViewModel: EmployeeViewModel   // ✅ declare here

    private var employeeList: List<EmployeeList> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_out_for_officework)

        spinnerReportingPerson = findViewById(R.id.spinnerReportingPerson)


        employeeViewModel = ViewModelProvider(this).get(EmployeeViewModel::class.java)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Observe ViewModel
        employeeViewModel.employeeResult.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    // TODO: Show loader
                }
                is UiState.Success -> {
                    employeeList = state.data?.employees ?: listOf()
                    if (employeeList.isNotEmpty()) {
                        setupSpinner(employeeList)
                    }
                }
                is UiState.Error -> {
                    Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        employeeViewModel.fetchEmployeeList()
    }

    private fun setupSpinner(list: List<EmployeeList>) {
        val employeeNames = list.map { it.fullName }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, employeeNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerReportingPerson.adapter = adapter

        spinnerReportingPerson.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedEmployee = list[position]
                Toast.makeText(
                    this@OutForOfficeWorkActivity,
                    "Selected: ${selectedEmployee.fullName} (ID: ${selectedEmployee.employeeId})",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
}
