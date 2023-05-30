package com.androrubin.genesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androrubin.genesis.databinding.ActivityDietPlanningBinding

class DietPlanning : AppCompatActivity() {


    private lateinit var binding : ActivityDietPlanningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityDietPlanningBinding.inflate(layoutInflater )
        setContentView(binding.root)

        binding.cardD1.setOnClickListener {
            val intent = Intent(this,DietChoice::class.java)
            startActivity(intent)

        }



    }
}