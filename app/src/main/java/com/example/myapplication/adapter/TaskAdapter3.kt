package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.MeetingItem
import com.example.myapplication.data.model.MeetingResponse
import com.example.myapplication.data.model.RoutineWorkItem
import com.example.myapplication.data.model.TaskItem
import com.example.myapplication.view.TaskDetailsActivity
import com.example.myapplication.view.WorkDetailsActivity


class TaskAdapter3(
    val list: List<TaskItem>

) : RecyclerView.Adapter<TaskAdapter3.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val taskName = itemView.findViewById<TextView>(R.id.txtTaskName)
        val taskResponsible = itemView.findViewById<TextView>(R.id.txtResponsibility)
        val scheduledDate = itemView.findViewById<TextView>(R.id.txtScheduledDate)
        val toleranceDate = itemView.findViewById<TextView>(R.id.txtToleranceDate)
        val timeRequired = itemView.findViewById<TextView>(R.id.txtTimeRequired)
        val mainlayout = itemView.findViewById<CardView>(R.id.mainlayout)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task3, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item = list[position]
        holder.taskName.text = item.taskName
        holder.taskResponsible.text = item.responsibility
        holder.scheduledDate.text = item.startDate
        holder.toleranceDate.text = item.completionDate
        holder.timeRequired.text = item.timeRequired.toString()

        holder.mainlayout.setOnClickListener {
            val intent = Intent(holder.itemView.context, TaskDetailsActivity::class.java)
            intent.putExtra("task_id", item.taskId)
            intent.putExtra("task_name", item.taskName)
            intent.putExtra("time_req", item.timeRequired)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}