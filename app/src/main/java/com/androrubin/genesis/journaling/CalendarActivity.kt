package com.androrubin.genesis.journaling

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androrubin.genesis.R
import com.google.firebase.database.collection.LLRBNode
import java.text.SimpleDateFormat
import java.time.Month
import java.time.MonthDay
import java.util.*

class CalendarActivity : AppCompatActivity() {

//    lateinit var compactCalendar: CompactCalendarView
//    private var dateFormat =SimpleDateFormat("MMMM-yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)


    }
}