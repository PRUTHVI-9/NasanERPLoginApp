package com.example.myapplication.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.MultiAutoCompleteTextView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import java.util.Calendar

class AddMeetingActivity : AppCompatActivity() {

    private lateinit var meetingSearch: AutoCompleteTextView
    private lateinit var meetingFrequency: Spinner
    private lateinit var venueSpinner: Spinner
    private lateinit var toleranceEditText: EditText

    private lateinit var oneTimeDateLayout: LinearLayout
    private lateinit var weeklyLayout: LinearLayout
    private lateinit var monthlyLayout: LinearLayout

    private lateinit var dateButton: Button

    private lateinit var dayMonday: CheckBox
    private lateinit var dayTuesday: CheckBox
    private lateinit var dayWednesday: CheckBox
    private lateinit var dayThursday: CheckBox
    private lateinit var dayFriday: CheckBox
    private lateinit var daySaturday: CheckBox
    private lateinit var daySunday: CheckBox

//    private lateinit var monthlyDatesInput: MultiAutoCompleteTextView
    private lateinit var monthlyDatesInput: EditText


    private lateinit var submitButton: Button

    private var selectedDate: String = ""

    private lateinit var selectEmployeesButton: Button
    private lateinit var selectedEmployeesText: TextView
    private val employeeList = listOf("Alice", "Bob", "Charlie", "David", "Emma", "Fatima", "George")
    private val selectedEmployees = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_meeting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Initialize views
        meetingSearch = findViewById(R.id.meetingSearch)
        meetingFrequency = findViewById(R.id.meetingFrequency)
        venueSpinner = findViewById(R.id.venueSpinner)
        toleranceEditText = findViewById(R.id.tolerance)

        oneTimeDateLayout = findViewById(R.id.oneTimeDateLayout)
        weeklyLayout = findViewById(R.id.weeklyLayout)
        monthlyLayout = findViewById(R.id.monthlyLayout)

        dateButton = findViewById(R.id.dateButton)

        dayMonday = findViewById(R.id.dayMonday)
        dayTuesday = findViewById(R.id.dayTuesday)
        dayWednesday = findViewById(R.id.dayWednesday)
        dayThursday = findViewById(R.id.dayThursday)
        dayFriday = findViewById(R.id.dayFriday)
        daySaturday = findViewById(R.id.daySaturday)
        daySunday = findViewById(R.id.daySunday)

//        monthlyDatesInput = findViewById(R.id.monthlyDatesInput)
        monthlyDatesInput = findViewById(R.id.monthlyDatesInput)

        submitButton = findViewById(R.id.submitButton)

        selectEmployeesButton = findViewById(R.id.selectEmployeesButton)
        selectedEmployeesText = findViewById(R.id.selectedEmployeesText)

        setupMeetingSearch()
        setupFrequencySpinner()
        setupVenueSpinner()
        setupDatePicker()
        setupSubmitButton()

        selectEmployeesButton.setOnClickListener {
            val checkedItems = BooleanArray(employeeList.size) { i -> selectedEmployees.contains(employeeList[i]) }

            AlertDialog.Builder(this)
                .setTitle("Select Employees")
                .setMultiChoiceItems(employeeList.toTypedArray(), checkedItems) { _, which, isChecked ->
                    val name = employeeList[which]
                    if (isChecked) {
                        if (!selectedEmployees.contains(name)) selectedEmployees.add(name)
                    } else {
                        selectedEmployees.remove(name)
                    }
                }
                .setPositiveButton("OK") { _, _ ->
                    selectedEmployeesText.text =
                        if (selectedEmployees.isNotEmpty())
                            "Selected: ${selectedEmployees.joinToString(", ")}"
                        else
                            "No employees selected"
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupMeetingSearch() {
        // Dummy meeting names for autocomplete
        val meetings = listOf("Sprint Planning", "Project Review", "Client Call", "Team Sync", "All Hands")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, meetings)
        meetingSearch.setAdapter(adapter)
    }

    private fun setupFrequencySpinner() {
        val frequencies = listOf("One Time", "Weekly", "Monthly", "Yearly")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, frequencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        meetingFrequency.adapter = adapter

        meetingFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: android.view.View?,
                position: Int, id: Long
            ) {
                val selected = parent.getItemAtPosition(position).toString()
                // Hide all first
                oneTimeDateLayout.visibility = android.view.View.GONE
                weeklyLayout.visibility = android.view.View.GONE
                monthlyLayout.visibility = android.view.View.GONE

                when (selected) {
                    "One Time" -> oneTimeDateLayout.visibility = android.view.View.VISIBLE
                    "Weekly" -> weeklyLayout.visibility = android.view.View.VISIBLE
                    "Monthly" -> monthlyLayout.visibility = android.view.View.VISIBLE
                    "Yearly" -> {
                        // Optional yearly layout if needed
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupVenueSpinner() {
        val venues = listOf(
            "Conference Room A",
            "Conference Room B",
            "Zoom",
            "Google Meet",
            "Microsoft Teams"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, venues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        venueSpinner.adapter = adapter
    }

    private fun setupDatePicker() {
        dateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                // m is zero-based
                selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                dateButton.text = selectedDate
            }, year, month, day)

            datePicker.show()
        }
    }

    private fun setupSubmitButton() {
        submitButton.setOnClickListener {
            val meetingName = meetingSearch.text.toString().trim()
            val frequency = meetingFrequency.selectedItem?.toString() ?: ""
            val venue = venueSpinner.selectedItem?.toString() ?: ""
            val tolerance = toleranceEditText.text.toString().trim()

            if (meetingName.isEmpty()) {
                Toast.makeText(this, "Please enter/select a meeting", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (tolerance.isEmpty()) {
                Toast.makeText(this, "Please enter tolerance in minutes", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var detailInfo = ""

            when (frequency) {
                "One Time" -> {
                    if (selectedDate.isEmpty()) {
                        Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    detailInfo = "Date: $selectedDate"
                }
                "Weekly" -> {
                    val selectedDays = mutableListOf<String>()
                    if (dayMonday.isChecked) selectedDays.add("Monday")
                    if (dayTuesday.isChecked) selectedDays.add("Tuesday")
                    if (dayWednesday.isChecked) selectedDays.add("Wednesday")
                    if (dayThursday.isChecked) selectedDays.add("Thursday")
                    if (dayFriday.isChecked) selectedDays.add("Friday")
                    if (daySaturday.isChecked) selectedDays.add("Saturday")
                    if (daySunday.isChecked) selectedDays.add("Sunday")

                    if (selectedDays.isEmpty()) {
                        Toast.makeText(this, "Please select at least one day", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    detailInfo = "Days: ${selectedDays.joinToString(", ")}"
                }
                "Monthly" -> {

                    val dates = monthlyDatesInput.text.toString().trim()
                    if (dates.isEmpty()) {
                        Toast.makeText(this, "Please enter dates for monthly meeting", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    detailInfo = "Dates: $dates"
                }
                "Yearly" -> {
                    // You can add yearly logic here
                    detailInfo = "Yearly frequency selected"
                }
            }

            val employeeInfo = if (selectedEmployees.isNotEmpty())
                "Employees: ${selectedEmployees.joinToString(", ")}"
            else
                "No employees selected"


            val summary = """
    Meeting: $meetingName
    Frequency: $frequency
    Venue: $venue
    Tolerance: $tolerance minutes
    $detailInfo
    $employeeInfo
""".trimIndent()


//            val summary = """
//                Meeting: $meetingName
//                Frequency: $frequency
//                Venue: $venue
//                Tolerance: $tolerance minutes
//                $detailInfo
//            """.trimIndent()

            Toast.makeText(this, summary, Toast.LENGTH_LONG).show()
        }
    }
}