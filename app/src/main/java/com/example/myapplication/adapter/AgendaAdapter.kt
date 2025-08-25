package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.AgendaList

class AgendaAdapter(private var agendaList: List<AgendaList>) :
    RecyclerView.Adapter<AgendaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val agendaText: TextView = itemView.findViewById(R.id.tvAgenda)
        val goalText: TextView = itemView.findViewById(R.id.tvGoal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_agenda, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val agenda = agendaList[position]
        holder.agendaText.text = agenda.agenda
        holder.goalText.text = agenda.goal
    }

    override fun getItemCount(): Int = agendaList.size

    fun updateData(newList: List<AgendaList>) {
        agendaList = newList
        notifyDataSetChanged()
    }
}
