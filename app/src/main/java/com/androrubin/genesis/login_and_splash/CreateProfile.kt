package com.androrubin.genesis.login_and_splash

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androrubin.genesis.MainActivity
import com.androrubin.genesis.R
import com.androrubin.genesis.databinding.ActivityCreateProfileBinding
import com.androrubin.genesis.ui.home.HomeFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class CreateProfile : AppCompatActivity() {

    private lateinit var binding:ActivityCreateProfileBinding
    private lateinit var db:FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    // lateinit var week_count_text:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // week_count_text=binding.weekCount.text.toString()





        db= FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val name = currentUser?.displayName
        val uid = currentUser?.uid

        val edtName = findViewById<TextInputEditText>(R.id.name_text1)
        val edtAge = findViewById<TextInputEditText>(R.id.age_text)









        binding.cardD1.setOnClickListener {

            if (TextUtils.isEmpty(edtName.getText()?.trim().toString())) {
                edtName.setError("Field cannot be empty")
                edtName.requestFocus()
            }else  if (TextUtils.isEmpty(edtAge.getText()?.trim().toString())) {
                edtAge.setError("Field cannot be empty")
                edtAge.requestFocus()
            }
            else{

                val data= hashMapOf(

                    "Name" to edtName.text?.trim().toString(),
                    "Age" to edtAge.text?.trim().toString(),
                    "ProfileCreated" to "1"

                )

                db.collection("Users").document("$uid")
                    .set(data)
                    .addOnSuccessListener {docRef ->
                        Log.d("Data Addition", "DocumentSnapshot written with ID: ${docRef}.id")
                        Toast.makeText(this,"Saved", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Data Addition", "Error adding document", e)
                    }



                val intent= Intent(this, WeekCountActivity::class.java)
                //     intent.putExtra("week_count",binding.weekCount.text.toString())
                intent.putExtra("name",edtName.text.toString())
                intent.putExtra("age",edtAge.text.toString())
                startActivity(intent)
                finish()
            }
        }

        binding.cardD2.setOnClickListener {

            if (TextUtils.isEmpty(edtName.getText()?.trim().toString())) {
                edtName.setError("Field cannot be empty")
                edtName.requestFocus()
            }else  if (TextUtils.isEmpty(edtAge.getText()?.trim().toString())) {
                edtAge.setError("Field cannot be empty")
                edtAge.requestFocus()
            }
            else{




                val intent= Intent(this, ChildAgeCount::class.java)
                intent.putExtra("name",edtName.text.toString())
                intent.putExtra("age",edtAge.text.toString())
                startActivity(intent)
                finish()
            }
        }


    }


}