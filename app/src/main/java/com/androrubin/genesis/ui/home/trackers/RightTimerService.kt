package com.androrubin.genesis.ui.home.trackers

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class RightTimerService : Service()
{
    override fun onBind(p0: Intent?): IBinder? = null

    private val timer2 = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        val time = intent.getDoubleExtra(TIME_EXTRA2, 0.0)
            timer2.scheduleAtFixedRate(TimeTask(time), 0, 1000)

        return START_STICKY
    }

    override fun onDestroy()
    {
        timer2.cancel()
        super.onDestroy()
    }

    private inner class TimeTask(private var time: Double) : TimerTask()
    {
        override fun run()
        {
            val intent = Intent(TIMER_UPDATED2)
            time++
            intent.putExtra(TIME_EXTRA2, time)
            sendBroadcast(intent)
        }
    }

    companion object
    {
        const val TIMER_UPDATED2 = "timerUpdated"
        const val TIME_EXTRA2 = "timeExtra"
    }

}