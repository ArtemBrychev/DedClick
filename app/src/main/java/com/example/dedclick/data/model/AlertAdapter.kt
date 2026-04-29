package com.example.dedclick.data.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dedclick.databinding.ItemAlertBinding

class AlertAdapter(
    private var items: List<HeartbeatDto>
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    class AlertViewHolder(val binding: ItemAlertBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemAlertBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val item = items[position]

        with(holder.binding) {
            titleText.text = "Check-in пропущен"
            subtitleText.text = "от контакта ${item.user.fullName}"
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<HeartbeatDto>) {
        items = newItems
        notifyDataSetChanged()
    }
}