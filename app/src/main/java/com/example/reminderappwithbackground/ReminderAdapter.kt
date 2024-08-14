package com.example.reminderappwithbackground

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    private val reminders = mutableListOf<Pair<String, Long>>()

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewReminder: TextView = itemView.findViewById(R.id.textViewReminderItem)
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        holder.textViewReminder.text = reminder.first
        val sdf = SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault())
        holder.textViewTime.text = sdf.format(Date(reminder.second))
    }

    override fun getItemCount(): Int = reminders.size

    fun addReminder(reminderText: String, timeInMillis: Long) {
        reminders.add(Pair(reminderText, timeInMillis))
        notifyDataSetChanged()
    }
}
