package com.androrubin.genesis.ui.aid.book_appointment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androrubin.genesis.databinding.ActivityScheduleAppointmentBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ScheduleAppointment : AppCompatActivity() {

    private lateinit var binding : ActivityScheduleAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var date : String = ""
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date = "$dayOfMonth/$month/$year"
        }
        val current = LocalDateTime.now()

        binding.calendarView.minDate = System.currentTimeMillis()

        binding.searchBtn.setOnClickListener {
            if(!binding.startHour.text.isEmpty() && !binding.startMinute.text.isEmpty() && !binding.endHour.text.isEmpty() && !binding.endMinute.text.isEmpty())
            {
                startActivity(Intent(this,BookAppointmentActivity::class.java).putExtra("Date",date).putExtra("Time" , "${binding.startHour.text}:${binding.startMinute.text} - ${binding.endHour.text}:${binding.endMinute.text}"))
            }
            else
            {
                Toast.makeText(this,"Enter the time of appointment",Toast.LENGTH_LONG).show()
            }
        }

        binding.backBtn.setOnClickListener {

            onBackPressed()

        }
    }
}