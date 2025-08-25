package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.MeetingList

class AddMeetingAdapter(
    private var meetings: List<MeetingList>
) : RecyclerView.Adapter<AddMeetingAdapter.MeetingViewHolder>(), Filterable {

    private var filteredMeetings: List<MeetingList> = meetings

    class MeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val meetingName: TextView = itemView.findViewById(R.id.meetingName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_meeting, parent, false)
        return MeetingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = filteredMeetings[position]
        holder.meetingName.text = meeting.meetingName


        holder.itemView.setOnClickListener {
            onItemClick?.invoke(meeting)
        }
    }

    override fun getItemCount(): Int = filteredMeetings.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newMeetings: List<MeetingList>) {
        meetings = newMeetings
        filteredMeetings = newMeetings
        Log.d("AddMeetingAdapter", "setData called with ${meetings.size} items")
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""
                val results = if (query.isEmpty()) {
                    meetings
                } else {
                    meetings.filter { it.meetingName.lowercase().contains(query) }
                }
                return FilterResults().apply { values = results }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredMeetings = results?.values as? List<MeetingList> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

    var onItemClick: ((MeetingList) -> Unit)? = null
}
