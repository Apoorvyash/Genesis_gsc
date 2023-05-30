package com.androrubin.genesis.ui.aid.book_caretaker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androrubin.genesis.databinding.ActivityScheduleAppointmentBinding
import com.androrubin.genesis.databinding.ActivityScheduleCaretakerBinding
import java.time.LocalDateTime


class ScheduleCareTaker : AppCompatActivity() {
    private lateinit var binding :  ActivityScheduleCaretakerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleCaretakerBinding.inflate(layoutInflater)
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
                startActivity(Intent(this, BookCareTakerActivity::class.java).putExtra("Date",date).putExtra("Time" , "${binding.startHour.text}:${binding.startMinute.text} - ${binding.endHour.text}:${binding.endMinute.text}"))
            }
            else
            {
                Toast.makeText(this,"Enter the time of appointment", Toast.LENGTH_LONG).show()
            }
        }

        binding.backBtn.setOnClickListener {

            onBackPressed()

        }
    }
}