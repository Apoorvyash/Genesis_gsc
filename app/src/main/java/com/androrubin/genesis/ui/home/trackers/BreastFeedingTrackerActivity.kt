package com.androrubin.genesis.ui.home.trackers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androrubin.genesis.MainActivity
import com.androrubin.genesis.R
import com.androrubin.genesis.databinding.ActivityBreastFeedingTrackerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import kotlin.math.roundToInt
import java.util.*

class BreastFeedingTrackerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreastFeedingTrackerBinding
    lateinit var dataHelper: DataHelper

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private val timer = Timer()
    private val timer2 = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreastFeedingTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataHelper = DataHelper(applicationContext)

        resetAction()
        mAuth  = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val currentUser = mAuth.currentUser
        val name = currentUser?.displayName
        val uid = currentUser?.uid

        val calendar = Calendar.getInstance()

        val current = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )

        binding.dayTV.text = SimpleDateFormat("EE", Locale.ENGLISH).format(calendar.getTime())
        binding.dateTv.text =
            "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${
                calendar.get(Calendar.YEAR)
            }"
        binding.timeTv.text = "${calendar.get(Calendar.HOUR)}:${calendar.get(Calendar.MINUTE)}"
        if(calendar.get(Calendar.HOUR_OF_DAY) >= 12)
        {
            binding.ampmTv.text = "pm"
        }
        else{
            binding.ampmTv.text = "am"
        }

        binding.leftStartBtn.setOnClickListener { startStopAction() }
        binding.rightStartBtn.setOnClickListener { rightStartStopAction() }
        binding.resetBtn.setOnClickListener { resetAction() }

        if (dataHelper.leftTimerCounting()) {
            startLeftTimer()
        } else {
            stopLeftTimer()
            if (dataHelper.startLeftTime() != null && dataHelper.stopLeftTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.leftTimerBtn.text = timeStringFromLong(time)
            }
        }

        if (dataHelper.rightTimerCounting()) {
            startRightTimer()
        } else {
            stopLeftTimer()
            if (dataHelper.startRightTime() != null && dataHelper.stopRightTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.rightTimerBtn.text = timeStringFromLong(time)
            }
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)
        //timer2.scheduleAtFixedRate(TimeTask2(), 0, 500)

        binding.saveRecordBtn.setOnClickListener {
            if (dataHelper.rightTimerCounting() || dataHelper.leftTimerCounting()) {
                Toast.makeText(
                    this,
                    "Please stop the timer before storing the data",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                val data = hashMapOf(

                    "Left Reading" to binding.leftTimerBtn.text.trim().toString(),
                    "Right Reading" to binding.rightTimerBtn.text.trim().toString(),
                    "Total Reading " to binding.totalTimerTV.text.trim().toString(),
                    "Date" to binding.dateTv.text.trim().toString(),
                    "Time" to binding.timeTv.text.trim().toString()

                )
                db.collection("Users").document("$uid").collection("Breast Feeding Records")
                    .add(data)
                    .addOnSuccessListener { docRef ->
                        Log.d("Data Addition", "DocumentSnapshot written with ID: ${docRef}.id")
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Data Addition", "Error adding document", e)
                    }
            }
        }
    }

private inner class TimeTask : TimerTask() {
    override fun run() {
        runOnUiThread() {
            var time1: Long = 0
            var time2: Long = 0
//                var time1 :Long = Date().time - dataHelper.startLeftTime()!!.time
//                var time2:Long = Date().time - dataHelper.startRightTime()!!.time
            if (dataHelper.leftTimerCounting()) {
                time1 = Date().time - dataHelper.startLeftTime()!!.time
                binding.leftTimerBtn.text = timeStringFromLong(time1)
            }

            if (dataHelper.rightTimerCounting()) {
                time2 = Date().time - dataHelper.startRightTime()!!.time
                binding.rightTimerBtn.text = timeStringFromLong(time2)
            }
            binding.totalTimerTV.text =
                addTime(binding.leftTimerBtn.text.toString(), binding.rightTimerBtn.text.toString())
            //binding.totalTimerTV.text = addTime(timeStringFromLong(time1),timeStringFromLong(time2))
//                    if(dataHelper.rightTimerCounting() || dataHelper.leftTimerCounting())
//                        binding.totalTimerTV.text = timeStringFromLong(Date().time - dataHelper.startRightTime()!!.time+Date().time - dataHelper.startLeftTime()!!.time)
            //binding.totalTimerTV.text = timeStringFromLong((Date().time - dataHelper.startRightTime()!!.time)+(Date().time - dataHelper.startLeftTime()!!.time))


        }
    }
}

private inner class TimeTask2 : TimerTask() {
    override fun run() {
        runOnUiThread() {
            if (dataHelper.rightTimerCounting()) {
                val time = Date().time - dataHelper.startRightTime()!!.time
                binding.rightTimerBtn.text = timeStringFromLong(time)
            }
        }
    }
}

private fun resetAction() {
    dataHelper.setLeftStopTime(null)
    dataHelper.setLeftStartTime(null)
    stopLeftTimer()
    binding.leftTimerBtn.text = timeStringFromLong(0)

    dataHelper.setRightStopTime(null)
    dataHelper.setRightStartTime(null)
    stopRightTimer()
    binding.rightTimerBtn.text = timeStringFromLong(0)

    binding.leftStartBtn.setImageResource(R.drawable.baseline_play_arrow_24)
    binding.rightStartBtn.setImageResource(R.drawable.baseline_play_arrow_24)
}

private fun stopLeftTimer() {
    dataHelper.setLeftTimerCounting(false)
    //binding.startBu.text = getString(R.string.start)
}

private fun startLeftTimer() {
    dataHelper.setLeftTimerCounting(true)
    //binding.startButton.text = getString(R.string.stop)
}

private fun startStopAction() {
    if (dataHelper.leftTimerCounting()) {
        dataHelper.setLeftStopTime(Date())
        binding.progressBar.pauseAnimation()
        binding.leftStartBtn.setImageResource(R.drawable.baseline_play_arrow_24)
        stopLeftTimer()
    } else {
        if (dataHelper.stopLeftTime() != null) {
            dataHelper.setLeftStartTime(calcRestartTime())
            binding.progressBar.resumeAnimation()
            dataHelper.setLeftStopTime(null)
        } else {
            dataHelper.setLeftStartTime(Date())
            binding.progressBar.playAnimation()
        }
        binding.leftStartBtn.setImageResource(R.drawable.baseline_pause_24)
        startLeftTimer()
    }
}

private fun calcRestartTime(): Date {
    val diff = dataHelper.startLeftTime()!!.time - dataHelper.stopLeftTime()!!.time
    return Date(System.currentTimeMillis() + diff)
}


private fun stopRightTimer() {
    dataHelper.setRightTimerCounting(false)
    //binding.startBu.text = getString(R.string.start)
}

private fun startRightTimer() {
    dataHelper.setRightTimerCounting(true)
    //binding.startButton.text = getString(R.string.stop)
}

private fun rightStartStopAction() {
    if (dataHelper.rightTimerCounting()) {
        dataHelper.setRightStopTime(Date())
        binding.rightStartBtn.setImageResource(R.drawable.baseline_play_arrow_24)
        binding.progressBar2.pauseAnimation()
        stopRightTimer()
    } else {
        if (dataHelper.stopRightTime() != null) {
            binding.progressBar2.resumeAnimation()
            dataHelper.setRightStartTime(calcRightRestartTime())
            dataHelper.setRightStopTime(null)
        } else {
            dataHelper.setRightStartTime(Date())
            binding.progressBar2.playAnimation()
        }
        binding.rightStartBtn.setImageResource(R.drawable.baseline_pause_24)
        startRightTimer()
    }
}

private fun calcRightRestartTime(): Date {
    val diff = dataHelper.startRightTime()!!.time - dataHelper.stopRightTime()!!.time
    return Date(System.currentTimeMillis() + diff)
}

private fun timeStringFromLong(ms: Long): String {
    val seconds = (ms / 1000) % 60
    val minutes = (ms / (1000 * 60) % 60)
    //val hours = (ms / (1000 * 60 * 60) % 24)
    return makeTimeString(minutes, seconds)
}

private fun makeTimeString(minutes: Long, seconds: Long): String {
    return String.format("%02d:%02d", minutes, seconds)
}

fun addTime(time1: String, time2: String): String {
    val list1 = time1.split(":")
    val list2 = time2.split(":")

    var X = list1[0].toLong() + list2[0].toLong()
    var y = list1[1].toLong() + list2[1].toLong()
    if (y > 60) {
        y = y - 60
        X = X + 1
    }

    return makeTimeString(X,y)
}

}