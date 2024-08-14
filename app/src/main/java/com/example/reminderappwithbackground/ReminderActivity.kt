package com.example.reminderappwithbackground

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat

class ReminderActivity : AppCompatActivity() {

    private val scheduleExactAlarmLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            scheduleNotification()
        } else {
            // Handle the case where the user denied the permission.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val textViewReminder = findViewById<TextView>(R.id.textViewReminder)
        val reminderText = intent.getStringExtra("REMINDER_TEXT")
        val reminderTime = intent.getLongExtra("REMINDER_TIME", 0)
        textViewReminder.text = reminderText

        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.SCHEDULE_EXACT_ALARM
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                scheduleExactAlarmLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
            } else {
                scheduleNotification()
            }
        } else {
            scheduleNotification()
        }
    }

    private fun scheduleNotification() {
        val reminderText = intent.getStringExtra("REMINDER_TEXT")
        val reminderTime = intent.getLongExtra("REMINDER_TIME", 0)
        val notificationIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("REMINDER_TEXT", reminderText)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "REMINDER_CHANNEL_ID",
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for reminder notifications"
                enableLights(true)
                lightColor = 0xFF00FF00.toInt()
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 1000)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
