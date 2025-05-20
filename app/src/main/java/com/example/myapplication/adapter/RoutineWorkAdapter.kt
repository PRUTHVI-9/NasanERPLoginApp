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
import com.example.myapplication.data.model.RoutineWorkItem
import com.example.myapplication.view.WorkDetailsActivity


class RoutineWorkAdapter constructor(
    val list: List<RoutineWorkItem>

) : RecyclerView.Adapter<RoutineWorkAdapter.ViewHolder>() {
    var context: Context? = null

    fun RoutineWorkAdapter(context: Context?) {
        this.context = context
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val routineName = itemView.findViewById<TextView>(R.id.txtRoutineName)
        val type = itemView.findViewById<TextView>(R.id.txtScheduleType)
        val date = itemView.findViewById<TextView>(R.id.txtScheduleDate)
        val tDate = itemView.findViewById<TextView>(R.id.txtToleranceDate)
        val timeRequired = itemView.findViewById<TextView>(R.id.txtTimeRequired)
        val mainlayout = itemView.findViewById<CardView>(R.id.mainlayout)
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
        holder.routineName.text = item.routineName
        holder.type.text =  item.scheduleType
        holder.date.text =  item.scheduleDate
        holder.tDate.text =item.toleranceDate
        holder.timeRequired.text =item.timeRequired
        holder.mainlayout.setOnClickListener {
            val intent: Intent = Intent(context, WorkDetailsActivity::class.java)
            context!!.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}