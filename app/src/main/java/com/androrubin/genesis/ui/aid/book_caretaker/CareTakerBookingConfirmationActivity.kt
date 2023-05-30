package com.androrubin.genesis.ui.aid.book_caretaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androrubin.genesis.MainActivity
import com.androrubin.genesis.databinding.ActivityCaretakerBookingConfirmationBinding

class CareTakerBookingConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCaretakerBookingConfirmationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaretakerBookingConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val specialisation = intent.getStringExtra("Specialization")
        val date = intent.getStringExtra("Date")
        val dn = intent.getStringExtra("DoctorName")

        binding.continueBtn.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.tvWithDn.text = "Your Appointment is Booked with\n" +
                "\\n $dn ( $specialisation )"

        binding.dateTv.text = "on $date"



    }

    override fun onBackPressed() {
        finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
    }
}