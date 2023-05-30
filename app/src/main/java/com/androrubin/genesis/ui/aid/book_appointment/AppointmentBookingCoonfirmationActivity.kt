package com.androrubin.genesis.ui.aid.book_appointment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androrubin.genesis.MainActivity
import com.androrubin.genesis.databinding.ActivityAppointmentPaymentBookingCoonfirmationBinding

class AppointmentBookingConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentPaymentBookingCoonfirmationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentPaymentBookingCoonfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val specialisation = intent.getStringExtra("Specialization")
        val date = intent.getStringExtra("Date")
        val dn = intent.getStringExtra("DoctorName")

        binding.continueBtn.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this,MainActivity::class.java))
        }
        binding.tvWithDn.text = "Your Appointment is Booked with\n" +
                "\\n $dn ( $specialisation )"

        binding.dateTv.text = "on $date"



    }

    override fun onBackPressed() {
        finishAffinity()
        startActivity(Intent(this,MainActivity::class.java))
    }
}