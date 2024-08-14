package com.example.reminderappwithbackground

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var reminderTime: Calendar
    private lateinit var adapter: ReminderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextReminder = findViewById<EditText>(R.id.editTextReminder)
        val buttonSetTime = findViewById<Button>(R.id.buttonSetTime)
        val buttonSetReminder = findViewById<Button>(R.id.buttonSetReminder)
        val recyclerViewReminders = findViewById<RecyclerView>(R.id.recyclerViewReminders)

        reminderTime = Calendar.getInstance()

        adapter = ReminderAdapter()
        recyclerViewReminders.layoutManager = LinearLayoutManager(this)
        recyclerViewReminders.adapter = adapter

        buttonSetTime.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, { _, selectedHour: Int, selectedMinute: Int ->
                reminderTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                reminderTime.set(Calendar.MINUTE, selectedMinute)
            }, hour, minute, true)

            timePicker.show()
        }

        buttonSetReminder.setOnClickListener {
            val reminderText = editTextReminder.text.toString()
            saveReminder(reminderText)
            adapter.addReminder(reminderText, reminderTime.timeInMillis)

            val intent = Intent(this, ReminderActivity::class.java)
            intent.putExtra("REMINDER_TEXT", reminderText)
            intent.putExtra("REMINDER_TIME", reminderTime.timeInMillis)
            startActivity(intent)
        }
    }

    private fun saveReminder(reminderText: String) {
        val sharedPreferences = getSharedPreferences("ReminderApp", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("reminder_$reminderText", reminderText)
        editor.putLong("time_$reminderText", reminderTime.timeInMillis)
        editor.apply()
    }
}
