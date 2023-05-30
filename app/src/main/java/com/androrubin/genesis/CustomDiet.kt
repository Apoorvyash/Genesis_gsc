package com.androrubin.genesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import com.androrubin.genesis.databinding.ActivityCustomDietBinding
import com.androrubin.genesis.diet.DietItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CustomDiet : AppCompatActivity() {

    private lateinit var binding: ActivityCustomDietBinding
    lateinit var carb_count: Array<Float>
    lateinit var prot_count: Array<Float>
    lateinit var fat_count: Array<Float>
    lateinit var qnt:Array<String>
    lateinit var qnt_val:Array<Int>
    lateinit var check_count: Array<CheckBox>
    lateinit var break_name:Array<String>
    private lateinit var db: DatabaseReference
    private lateinit var mAuth: FirebaseAuth





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomDietBinding.inflate(layoutInflater)
        setContentView(binding.root)



        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val name = currentUser?.displayName
        val uid = currentUser?.uid

//        var data = hashMapOf<String,DietItem>()

        break_name= arrayOf(
            "Chapati/Roti",
            "Boiled Egg",
            "Oats"
        )



        carb_count = arrayOf(
            20.0f,
            0.6f,
            103f
        )
        prot_count =
            arrayOf(
                4.0f,
                6.3f,
                26.35f
            )
        fat_count =
            arrayOf(
                1f,
                2f,
                11f
            )

        check_count= arrayOf(
            binding.checkBox1,
            binding.checkBox2,
            binding.checkBox3
        )

        qnt= arrayOf(
            binding.qn1Ed.text!!.toString(),
            binding.qn2Ed.text!!.toString(),
            binding.qn3Ed.text!!.toString()
        )


         qnt_val= arrayOf(1,1,1)







        binding.check.setOnClickListener {



            for( i in 1..3){
                if(qnt[i-1]!="")
                {
                    qnt_val[i-1]=qnt[i-1].toInt()
                }


            }



            var carb_Sum = 0f
            var pro_Sum = 0f
            var fat_Sum = 0f
            db= FirebaseDatabase.getInstance().getReference(uid.toString())

            for (i in 1..3) {

                if(check_count[i-1].isChecked)
                {
                    carb_Sum=carb_Sum+(carb_count[i-1]*qnt_val[i-1])
                    pro_Sum=pro_Sum+(prot_count[i-1]*qnt_val[i-1])
                    fat_Sum=fat_Sum+(fat_count[i-1]*qnt_val[i-1])

                    db.child("Breakfast").child("item$i").setValue(DietItem(break_name[i-1],qnt_val[i-1]))

                }

            }


            Toast.makeText(this,"carb=$carb_Sum,  pro=$pro_Sum,  fat=$fat_Sum",Toast.LENGTH_SHORT).show()

            val intent = Intent(this,NutriComparision::class.java)
            intent.putExtra("carb_count",carb_Sum)
            intent.putExtra("pro_count",pro_Sum)
            intent.putExtra("fat_count",fat_Sum)
            startActivity(intent)


        }
    }


}