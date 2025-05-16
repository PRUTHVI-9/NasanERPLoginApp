package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.RoutineWorkItem


class RoutineWorkAdapter constructor(
    val list: List<RoutineWorkItem>
) : RecyclerView.Adapter<RoutineWorkAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val routineName = itemView.findViewById<TextView>(R.id.txtRoutineName)
        val type = itemView.findViewById<TextView>(R.id.txtScheduleType)
        val date = itemView.findViewById<TextView>(R.id.txtScheduleDate)
        val tDate = itemView.findViewById<TextView>(R.id.txtToleranceDate)
        val timeRequired = itemView.findViewById<TextView>(R.id.txtTimeRequired)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine_work, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item = list[position]
        holder.routineName.text = "Routine Name: " + item.routineName
        holder.type.text = "Schedule Type: " + item.scheduleType
        holder.date.text = "Scheduled Date: " + item.scheduleDate
        holder.tDate.text = "Tolerance Date: " + item.toleranceDate
        holder.timeRequired.text = "Time Req: " + item.timeRequired
    }

    override fun getItemCount(): Int {
        return list.size
    }


}