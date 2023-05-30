package com.androrubin.genesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class DietChoice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_choice)

        val card_explore= findViewById<CardView>(R.id.card_d4)
        card_explore.setOnClickListener {
            startActivity(Intent(this,CustomDiet::class.java))
        }
    }
}