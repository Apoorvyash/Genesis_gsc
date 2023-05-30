package com.androrubin.genesis.ui.aid.book_caretaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androrubin.genesis.databinding.ActivityCaretakerPaymentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CareTakerPaymentActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    private lateinit var binding: ActivityCaretakerPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaretakerPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = intent.getStringExtra("Date")
        val time = intent.getStringExtra("Time")
        val dn = intent.getStringExtra("DoctorName")
        val sp = intent.getStringExtra("Speciality")
        val yoe = intent.getStringExtra("Yoe")

        binding.dateTimeTV.text = date
        binding.doctorNameTv.text = dn
        binding.specializationTv.text = sp
        binding.yoeTV.text = yoe
        binding.timeTv.text = time

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val name = currentUser?.displayName
        val uid = currentUser?.uid

        binding.radioButton.setOnClickListener {

            if (binding.radioButton2.isChecked) {
                binding.radioButton2.isChecked = false
            }
            binding.textView53.text = "₹ 2000"
        }

        binding.radioButton2.setOnClickListener {
            if (binding.radioButton.isChecked) {
                binding.radioButton.isChecked = false
            }

            binding.textView53.text = "₹ 2400"
        }


        binding.makePaymentBtn.setOnClickListener {

            if (binding.textView53.text != "₹ 0") {

                val data = hashMapOf(

                    "date" to date,
                    "time" to time,
                    "doctorName " to dn,
                    "specialisation" to sp,
                    "yoe" to yoe

                )
                db = FirebaseFirestore.getInstance()

                db.collection("Users").document("$uid").collection("LastBookedCareTaker")
                    .document("Appointment1")
                    .set(data)
                    .addOnSuccessListener { docRef ->
                        Log.d("Data Addition", "DocumentSnapshot written with ID: ${docRef}.id")
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Data Addition", "Error adding document", e)
                    }

                startActivity(
                    Intent(this, CareTakerBookingConfirmationActivity::class.java)
                        .putExtra("DoctorName", binding.doctorNameTv.text.toString())
                        .putExtra("Date",binding.dateTimeTV.text.toString())
                        .putExtra("Specialization", sp)
                )
                finish()
            }
        }
    }
}