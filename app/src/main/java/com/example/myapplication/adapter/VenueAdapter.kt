package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.VenueList


class VenueAdapter(
    private var venues: List<VenueList>,
    private val onItemClick: (VenueList) -> Unit
) : RecyclerView.Adapter<VenueAdapter.VenueViewHolder>() {

    inner class VenueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val venueNameText: TextView = itemView.findViewById(R.id.VenueName)

        fun bind(venue: VenueList) {
            venueNameText.text = venue.venueName
            itemView.setOnClickListener {
                onItemClick(venue)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venue, parent, false)
        return VenueViewHolder(view)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.bind(venues[position])
    }

    override fun getItemCount(): Int = venues.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newVenues: List<VenueList>) {
        venues = newVenues
        notifyDataSetChanged()
    }
}
