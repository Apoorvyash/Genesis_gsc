package com.androrubin.genesis.ui.home.trackers
import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class DataHelper(context: Context)
{
    private var sharedPref :SharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    private var dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())

    var timerCounting = false
    private var startTime: Date? = null
    private var stopTime: Date? = null

    private var rightTimerCounting = false
    private var rightStartTime: Date? = null
    private var rightStopTime: Date? = null

    init
    {
        timerCounting = sharedPref.getBoolean(LEFT_COUNTING_KEY, false)

        val startString = sharedPref.getString(LEFT_START_TIME_KEY, null)
        if (startString != null)
            startTime = dateFormat.parse(startString)

        val stopString = sharedPref.getString(LEFT_STOP_TIME_KEY, null)
        if (stopString != null)
            stopTime = dateFormat.parse(stopString)


        rightTimerCounting = sharedPref.getBoolean(RIGHT_COUNTING_KEY, false)

        val startString2 = sharedPref.getString(RIGHT_START_TIME_KEY, null)
        if (startString2 != null)
            rightStartTime = dateFormat.parse(startString2)

        val stopString2 = sharedPref.getString(RIGHT_STOP_TIME_KEY, null)
        if (stopString2 != null)
            rightStopTime = dateFormat.parse(stopString2)
    }


    fun startLeftTime(): Date? = startTime

    fun startRightTime(): Date? = rightStartTime

    fun setLeftStartTime(date: Date?)
    {
        startTime = date
        with(sharedPref.edit())
        {
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(LEFT_START_TIME_KEY,stringDate)
            apply()
        }
    }

    fun setRightStartTime(date: Date?)
    {
        rightStartTime = date
        with(sharedPref.edit())
        {
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(RIGHT_START_TIME_KEY,stringDate)
            apply()
        }
    }

    fun stopLeftTime(): Date? = stopTime

    fun stopRightTime(): Date? = rightStopTime

    fun setLeftStopTime(date: Date?)
    {
        stopTime = date
        with(sharedPref.edit())
        {
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(LEFT_STOP_TIME_KEY,stringDate)
            apply()
        }
    }

    fun setRightStopTime(date: Date?)
    {
        rightStopTime = date
        with(sharedPref.edit())
        {
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(RIGHT_STOP_TIME_KEY,stringDate)
            apply()
        }
    }

    fun leftTimerCounting(): Boolean = timerCounting

    fun rightTimerCounting(): Boolean = rightTimerCounting

    fun setLeftTimerCounting(value: Boolean)
    {
        timerCounting = value
        with(sharedPref.edit())
        {
            putBoolean(LEFT_COUNTING_KEY,value)
            apply()
        }
    }

    fun setRightTimerCounting(value: Boolean)
    {
        rightTimerCounting = value
        with(sharedPref.edit())
        {
            putBoolean(RIGHT_COUNTING_KEY,value)
            apply()
        }
    }

    companion object
    {
        const val PREFERENCES = "prefs"
        const val LEFT_START_TIME_KEY = "leftStartKey"
        const val LEFT_STOP_TIME_KEY = "leftStopKey"
        const val LEFT_COUNTING_KEY = "leftCountingKey"
        const val RIGHT_START_TIME_KEY = "rightStartKey"
        const val RIGHT_STOP_TIME_KEY = "rightStopKey"
        const val RIGHT_COUNTING_KEY = "rightCountingKey"
    }
}