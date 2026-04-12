package com.example.dedclick.data.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dedclick.databinding.ItemElderPersonBinding
import com.example.dedclick.databinding.ItemTrustedPersonBinding

class ElderAdapter(
    private val items: List<ContactDto>,
    private val onDeleteClick: (ContactDto) -> Unit,
    private val onInfoClick: (ContactDto) -> Unit
) : RecyclerView.Adapter<ElderAdapter.ElderViewHolder>() {

    inner class ElderViewHolder(
        val binding: ItemElderPersonBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElderViewHolder {
        val binding = ItemElderPersonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ElderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ElderViewHolder, position: Int) {
        val contact = items[position]

        val user = contact.member

        holder.binding.personName.text = user.fullName
        holder.binding.personPhone.text = user.username

        holder.binding.deleteButton.setOnClickListener {
            onDeleteClick(contact)
        }
    }
}