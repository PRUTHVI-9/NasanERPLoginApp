// AddMeetingActivity.kt
package com.example.myapplication.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.AddMeetingAdapter
import com.example.myapplication.adapter.AgendaAdapter
//import com.example.myapplication.adapter.AgendaAdapter
import com.example.myapplication.adapter.VenueAdapter
import com.example.myapplication.data.model.EmployeeList
import com.example.myapplication.utils.UiState
import com.example.myapplication.viewModels.AddMeetingViewModel
import com.example.myapplication.viewModels.AgendaViewModel
//import com.example.myapplication.viewModels.AgendaViewModel
import com.example.myapplication.viewModels.EmployeeViewModel
import com.example.myapplication.viewModels.VenueViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddMeetingActivity : AppCompatActivity() {

    private lateinit var meetingSearch: AutoCompleteTextView
    private lateinit var meetingFrequency: Spinner
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

    private lateinit var meetingRecyclerView: RecyclerView
    private lateinit var meetingAdapter: AddMeetingAdapter
    private lateinit var addMeetingViewModel: AddMeetingViewModel // renamed

    private lateinit var venueViewModel: VenueViewModel
    private lateinit var venueAdapter: VenueAdapter


    private lateinit var monthlyDatesInput: EditText
    private lateinit var submitButton: Button

    private var selectedDate: String = ""

    private lateinit var selectEmployeeButton: Button
    private lateinit var selectedEmployeesText: TextView
    private lateinit var employeeViewModel: EmployeeViewModel
    private var employeeList: List<EmployeeList> = listOf()
    private val selectedEmployees = mutableListOf<EmployeeList>()

    private var selectedVenueName: String = ""

    private lateinit var etMeetingLink: EditText
    private lateinit var tvMeetingLink: TextView

    private lateinit var agendaViewModel: AgendaViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var agendaAdapter : AgendaAdapter


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_meeting)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        meetingSearch = findViewById(R.id.meetingSearch)
        meetingFrequency = findViewById(R.id.meetingFrequency)
        toleranceEditText = findViewById(R.id.tolerance)

        etMeetingLink = findViewById(R.id.etMeetingLink)
        tvMeetingLink = findViewById(R.id.tvMeetingLink)

        etMeetingLink.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val link = etMeetingLink.text.toString().trim()
                if (Patterns.WEB_URL.matcher(link).matches()) {
                    tvMeetingLink.text = link
                } else {
                    tvMeetingLink.text = "❌ Invalid link"
                }
            }
        }

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

        meetingRecyclerView = findViewById(R.id.meetingRecyclerView)
        monthlyDatesInput = findViewById(R.id.monthlyDatesInput)
        submitButton = findViewById(R.id.submitButton)

        selectEmployeeButton = findViewById(R.id.selectEmployeesButton)
        selectedEmployeesText = findViewById(R.id.selectedEmployeesText)
        recyclerView = findViewById(R.id.recyclerview_agenda)

        addMeetingViewModel = ViewModelProvider(this)[AddMeetingViewModel::class.java]
        venueViewModel = ViewModelProvider(this)[VenueViewModel::class.java]
        employeeViewModel = ViewModelProvider(this)[EmployeeViewModel::class.java]
        agendaViewModel = ViewModelProvider(this)[AgendaViewModel::class.java]

        meetingAdapter = AddMeetingAdapter(listOf())
        meetingRecyclerView.layoutManager = LinearLayoutManager(this)
        meetingRecyclerView.adapter = meetingAdapter



        val selectedMeeting: TextView = findViewById(R.id.selectedMeeting)

        addMeetingViewModel.fetchMeetingDesc()
        addMeetingViewModel.result.observe(this) { result ->
            when (result) {
                is UiState.Loading -> Log.d("MeetingData", "Loading meeting data...")
                is UiState.Success -> {
                    val meetings = result.data?.meetings
                    if (meetings != null) {
                        meetingAdapter.setData(meetings)
                        meetingAdapter.onItemClick = { meeting ->
                            selectedMeeting.text = meeting.meetingName

                            meeting.meetingDescId.let { id ->
                                agendaViewModel.fetchAgenda(id)
                            }
                        }
                        meetingSearch.addTextChangedListener { text ->
                            meetingAdapter.filter.filter(text)
                        }
                    }
                }
                is UiState.Error -> {
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        val venueRecyclerView = findViewById<RecyclerView>(R.id.venueRecyclerView)
        venueRecyclerView.layoutManager = LinearLayoutManager(this)
        venueAdapter = VenueAdapter(listOf()) { selectedVenue ->
            selectedVenueName = selectedVenue.venueName
            Toast.makeText(this, "Selected: ${selectedVenue.venueName}", Toast.LENGTH_SHORT).show()
        }
        venueRecyclerView.adapter = venueAdapter

        venueViewModel.venueResult.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val venuesList = state.data?.venue
                    if (venuesList != null) venueAdapter.updateData(venuesList)
                }
                is UiState.Error -> {
                    Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        venueViewModel.fetchVenueList()

        employeeViewModel.employeeResult.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Success -> {
                    employeeList = state.data?.employees ?: listOf()
                }
                is UiState.Error -> {
                    Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        employeeViewModel.fetchEmployeeList()




        selectEmployeeButton.setOnClickListener {
            showEmployeeSelectionDialog()
        }

        val checkPostponed = findViewById<CheckBox>(R.id.checkPostponed)
        val rescheduleLayout = findViewById<LinearLayout>(R.id.rescheduleLayout)
        val btnRescheduleDate = findViewById<Button>(R.id.btnRescheduleDate)

        checkPostponed.setOnCheckedChangeListener { _, isChecked ->
            rescheduleLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        btnRescheduleDate.setOnClickListener {
            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                btnRescheduleDate.text = "$dayOfMonth/${month + 1}/$year"
            }, 2025, 0, 1)
            datePicker.show()
        }

        agendaAdapter = AgendaAdapter(listOf())
        recyclerView.adapter = agendaAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 👉 Intent MeetingDescId
        val meetingDescId = intent.getStringExtra("MeetingDescId") ?: ""
        print("meetingDesc$meetingDescId")

        // LiveData observe
        agendaViewModel.agenda.observe(this) { agendaResponse ->
            Log.d("AGENDA_DATA", "Data received: ${agendaResponse?.agenda?.size}")
            agendaResponse?.let {
                agendaAdapter.updateData(it.agenda)
            }
        }

        // API call
        if (meetingDescId.isNotEmpty()) {
            agendaViewModel.fetchAgenda(meetingDescId)
        }



        setupFrequencySpinner()
        setupDatePicker()
        setupSubmitButton()
    }

    private fun showEmployeeSelectionDialog() {
        if (employeeList.isEmpty()) {
            Toast.makeText(this, "No employees available", Toast.LENGTH_SHORT).show()
            return
        }

        val employeeNames = employeeList.map { it.fullName }.toTypedArray()
        val checkedItems = BooleanArray(employeeList.size) { index ->
            selectedEmployees.any { it.employeeId == employeeList[index].employeeId }
        }

        AlertDialog.Builder(this)
            .setTitle("Select Employees For Meeting")
            .setMultiChoiceItems(employeeNames, checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    selectedEmployees.add(employeeList[which])
                } else {
                    selectedEmployees.removeAll { it.employeeId == employeeList[which].employeeId }
                }
            }
            .setPositiveButton("OK") { _, _ ->
                selectedEmployeesText.text = if (selectedEmployees.isNotEmpty()) {
                    "Selected: ${selectedEmployees.joinToString(", ") { it.fullName }}"
                } else {
                    "No employees selected"
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupFrequencySpinner() {
        val frequencies = listOf("One Time", "Weekly", "Monthly", "Yearly")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, frequencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        meetingFrequency.adapter = adapter

        meetingFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = parent.getItemAtPosition(position).toString()
                oneTimeDateLayout.visibility = View.GONE
                weeklyLayout.visibility = View.GONE
                monthlyLayout.visibility = View.GONE

                when (selected) {
                    "One Time" -> oneTimeDateLayout.visibility = View.VISIBLE
                    "Weekly" -> weeklyLayout.visibility = View.VISIBLE
                    "Monthly" -> monthlyLayout.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setupDatePicker() {
        dateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                dateButton.text = selectedDate
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }
    }

    private fun setupSubmitButton() {
        submitButton.setOnClickListener {
            val meetingName = meetingSearch.text.toString().trim()
            val frequency = meetingFrequency.selectedItem?.toString() ?: ""
            val tolerance = toleranceEditText.text.toString().trim()
            val meetingLink = etMeetingLink.text.toString().trim()

            if (meetingName.isEmpty()) {
                Toast.makeText(this, "Please enter/select a meeting", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (tolerance.isEmpty()) {
                Toast.makeText(this, "Please enter tolerance in minutes", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.WEB_URL.matcher(meetingLink).matches()) {
                Toast.makeText(this, "Please enter a valid meeting link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val detailInfo = when (frequency) {
                "One Time" -> {
                    if (selectedDate.isEmpty()) {
                        Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    "Date: $selectedDate"
                }
                "Weekly" -> {
                    val days = mutableListOf<String>()
                    if (dayMonday.isChecked) days.add("Monday")
                    if (dayTuesday.isChecked) days.add("Tuesday")
                    if (dayWednesday.isChecked) days.add("Wednesday")
                    if (dayThursday.isChecked) days.add("Thursday")
                    if (dayFriday.isChecked) days.add("Friday")
                    if (daySaturday.isChecked) days.add("Saturday")
                    if (daySunday.isChecked) days.add("Sunday")
                    if (days.isEmpty()) {
                        Toast.makeText(this, "Please select at least one day", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    "Days: ${days.joinToString(", ")}"
                }
                "Monthly" -> {
                    val dates = monthlyDatesInput.text.toString().trim()
                    if (dates.isEmpty()) {
                        Toast.makeText(this, "Please enter dates for monthly meeting", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    "Dates: $dates"
                }
                "Yearly" -> "Yearly frequency selected"
                else -> ""
            }

            val employeeInfo = if (selectedEmployees.isNotEmpty()) {
                "Employees: ${selectedEmployees.joinToString(", ") { it.fullName }}"
            } else {
                "No employees selected"
            }

            val summary = """
                Meeting: $meetingName
                Frequency: $frequency
                Venue: ${if (selectedVenueName.isNotEmpty()) selectedVenueName else "No venue selected"}
                Tolerance: $tolerance minutes
                Link: $meetingLink
                $detailInfo
                $employeeInfo
            """.trimIndent()

            Toast.makeText(this, summary, Toast.LENGTH_LONG).show()
        }
    }
}
