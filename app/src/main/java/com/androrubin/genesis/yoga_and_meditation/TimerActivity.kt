package com.androrubin.genesis.yoga_and_meditation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.androrubin.genesis.R
import com.androrubin.genesis.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private lateinit var countdown_timer: CountDownTimer
    private var isRunning: Boolean = true
    private var resting: Boolean = false
    private var p1: Int = 0
    private var tottime: Int = 0
    private var run: Int = 0
    private var initial_time: String = "Not defined yet"
    private var resting_time: String = "Not defined yet"
    private lateinit var mediaPlayer: MediaPlayer
    private var perc_progress = 0
    val openURL = Intent(Intent.ACTION_VIEW)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // compute total time if value of block or workout time is updated
        binding.mainEdittextTime.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim({ it <= ' ' }).isEmpty()) {
                    binding.mainEdittextTime.setText("1")
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.toString().trim({ it <= ' ' }).isEmpty())
                {
                    binding.mainButtonLaunch.setEnabled(false)
                }
                else
                {
                    binding.mainButtonLaunch.setEnabled(true)
                }
                if (binding.mainEdittextTime.text.toString().toIntOrNull() != null) {
                    compute_totaltime()
                }
            }
        })

        binding.mainEdittextRest.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim({ it <= ' ' }).isEmpty()) {
                    binding.mainEdittextRest.setText("0")
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })


        binding.mainEdittextRun.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim({ it <= ' ' }).isEmpty()) {
                    binding.mainEdittextRun.setText("1")
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (binding.mainEdittextRun.text.toString().toIntOrNull() != null) {
                    compute_totaltime()
                }
            }
        })

        binding.imgLogo.setOnClickListener {
            openURL.data = Uri.parse("https://losangebleu.site/")
            startActivity(openURL)
        }
        binding.mainButtonLaunch.setOnClickListener {
            hideKeyboard()
            binding.llSecondSection.visibility = View.VISIBLE
            binding.llTopSection.visibility = View.GONE

            if (binding.mainEdittextTime.text.toString().toIntOrNull() != null) {
                initial_time = binding.mainEdittextTime.text.toString()
            } else {
                initial_time = "50"
            }
            if (binding.mainEdittextRest.text.toString().toIntOrNull() != null) {
                resting_time = binding.mainEdittextRest.text.toString()
            } else {
                resting_time = "10"
            }
            if (binding.mainEdittextRun.text.toString().toIntOrNull() != null) {
                run = binding.mainEdittextRun.text.toString().toInt() - 1
                if (binding.mainEdittextRun.text.toString().toInt() == 0) {
                    run = 0
                }
            } else {
                run = 12
            }
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.start)
            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            mediaPlayer.start()
            start_timer(resting)

        }
        binding.timerActivityResetbutton.setOnClickListener {
            reset_timer()
        }
        binding.timerActivityPausebutton.setOnClickListener {
            if (isRunning) {
                pause_timer()
            } else {
                continue_timer()
            }
        }
    }

    // compute total time on first screen
    private fun compute_totaltime() {
        tottime = binding.mainEdittextTime.text.toString().toInt() * binding.mainEdittextRun.text.toString().toInt()
        tottime = tottime / 60
        binding.totaltime.text = "About $tottime min"
    }

    // Define what is done when button "start timer" is clicked
    private fun start_timer(rest: Boolean) {
        // resting time timer
        if (rest) {
            startClock(resting_time)
            // working time timer
        } else {
            display_run(run.toString())
            startClock(initial_time)
        }
    }

    // Restart the app
    private fun reset_timer() {
        countdown_timer.cancel()
        val intent = intent
        finish()
        startActivity(intent)
    }

    // Define what is done when button "pause timer" is clicked
    private fun pause_timer() {
        isRunning = false
        binding.tvTypework.text = " "
        val pause_time = p1.toString()
        display_clock("Paused ($pause_time s)")
        binding.timerActivityPausebutton.text = "Continue"
        countdown_timer.cancel()
    }

    private fun continue_timer() {
        if (resting) {
            binding.tvTypework.text = "Rest for "
        } else {
            binding.tvTypework.text = "Workout for "
        }
        binding.tvTypework.text = ""
        startClock(p1.toString())
    }

    // Show time left when timer is started
    private fun display_clock(time: String) {
        binding.tvCounter.text = time
    }

    // Show nb of run left when timer is started
    private fun display_run(run_nb: String) {
        binding.tvRun.text = "Only $run_nb block(s) left"
    }

    private fun update_progressBar() {
        if (resting) {
            perc_progress = 100 - (p1 * 100) / (resting_time.toInt())
        } else {
            perc_progress = 100 - (p1 * 100) / (initial_time.toInt())
        }
        binding.progressBar.progress = perc_progress
    }

    // Hide keyboard functions
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }
    @SuppressLint("ServiceCast")
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun finish_timer() {
        binding.tvRun.visibility = View.GONE
        binding.tvCounter.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.imgRest.visibility = View.GONE
        binding.imgWork.visibility = View.GONE
        binding.tvTypework.visibility = View.GONE
        binding.timerActivityPausebutton.visibility = View.GONE
        binding.timerActivityTextviewTitle.text = "Congrats, be a proud mother !"
        display_clock("DONE !")
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.finish)

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        mediaPlayer.start()
    }

    // launch underlying countdown timer for working time
    private fun startClock(number: String) {
        binding.timerActivityPausebutton.text = "  Pause  "
        isRunning = true
        countdown_timer = object : CountDownTimer((number.toLong() -1) * 1000, 1000) {
            override fun onTick(p0: Long) {
                p1 = p0.toInt() / 1000 + 1
                update_progressBar()
                // resting time
                if (resting) {
                    binding.tvTypework.text = "Rest for  "
                    binding.imgRest.visibility = View.VISIBLE
                    binding.imgWork.visibility = View.GONE
                    display_clock(p1.toString() + " sec(s)")
                    // working time
                } else {
                    binding.tvTypework.text = "Workout for  "
                    binding.imgRest.visibility = View.GONE
                    binding.imgWork.visibility = View.VISIBLE
                    display_clock(p1.toString() + " sec(s)")
                }
            }

            override fun onFinish() {
                // finish resting session
                if (resting)  {
                    run -= 1
                    // last round
                    if (run <= -1) {
                        finish_timer()
                        // still some runs left
                    } else {
                        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.start)
                        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
                        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                        mediaPlayer.start()
                        resting = false
                        // start working timer
                        start_timer(resting)
                    }

                    // finish working session
                } else {
                    if (resting_time.toInt() == 0) {
                        run -= 1
                        // last round
                        if (run <= -1)  {
                            finish_timer()
                            // still some runs left
                        } else {
                            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.start)
                            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
                            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                            mediaPlayer.start()
                            // start working timer
                            start_timer(resting)
                        }

                    } else {
                        resting = true
                        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.next)
                        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
                        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                        mediaPlayer.start()
                        start_timer(resting)
                    }

                }
            }

        }
        countdown_timer.start()
    }

}