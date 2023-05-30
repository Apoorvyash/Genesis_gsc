package com.androrubin.genesis.login_and_splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.androrubin.genesis.MainActivity
import com.androrubin.genesis.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChildAgeCount : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_child_age_count)
        db= FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.currentUser
        val user_name = currentUser?.displayName
        val uid = currentUser?.uid


        val name= intent.getStringExtra("name")
        val age = intent.getStringExtra("age")
        val  edtAge = findViewById<EditText>(R.id.child_age)
        val  next_btn = findViewById<TextView>(R.id.nextBtn2)

        next_btn.setOnClickListener {

            if (TextUtils.isEmpty(edtAge.getText()?.trim().toString())) {
                edtAge.setError("Field cannot be empty")
                edtAge.requestFocus()
            }else{



                val data= hashMapOf(

                    "Name" to name?.trim(),
                    "Age" to age?.trim(),
                    "Child Age" to edtAge.text?.trim().toString(),
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

            }
            startActivity(Intent(this, MainActivity::class.java))


        }


    }

}
