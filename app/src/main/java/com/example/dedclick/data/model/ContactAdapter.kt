package com.example.dedclick.data.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dedclick.databinding.ItemTrustedPersonBinding

class ContactsAdapter(
    private val items: List<ContactDto>,
    private val onDeleteClick: (ContactDto) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(
        val binding: ItemTrustedPersonBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemTrustedPersonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = items[position]

        val user = contact.keeper

        holder.binding.personName.text = user.fullName
        holder.binding.personPhone.text = user.username

        holder.binding.deleteButton.setOnClickListener {
            onDeleteClick(contact)
        }
    }
}