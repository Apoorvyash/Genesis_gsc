package com.androrubin.genesis.ui.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.androrubin.genesis.MainActivity
import com.androrubin.genesis.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CreatePostActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        supportActionBar?.hide()
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val uid = mAuth.currentUser?.uid

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser




        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
        val current = formatter.format(time)

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)
        val postBtn = findViewById<TextView>(R.id.post_Btn)
        val posterName = findViewById<TextView>(R.id.posterName)
        val postDescription = findViewById<EditText>(R.id.postDescriptionEdt)

        if (user != null)
        {
            val name = user.uid
            db = FirebaseFirestore.getInstance()
            db.collection("Users").document("$name")
                .get()
                .addOnSuccessListener {

                    //Returns value of corresponding field
                    val b = it["Name"].toString()

                    posterName.text = b

                }
        }

        closeBtn.setOnClickListener {
            onBackPressed()
        }

        postBtn.setOnClickListener {
            if (TextUtils.isEmpty(postDescription.getText()?.trim().toString())) {
                postDescription.setError("Field cannot be empty")
                postDescription.requestFocus()
            } else {
                val data = hashMapOf(
                    "postID" to "$uid$current",
                    "posterName" to posterName.text.trim().toString(),
                    "description" to postDescription.text.trim().toString(),
                    "date" to current.toString(),
                    "upvotes" to "0",
                    "downvotes" to "0",
                    "comments" to "0"
                )
                db.collection("Community Posts").document("$uid$current")
                    .set(data)
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
    }