package com.androrubin.genesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.databinding.ActivityNutriComparisionBinding
import com.androrubin.genesis.diet.DietItem
import com.androrubin.genesis.diet.DietItemAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class NutriComparision : AppCompatActivity() {
    private lateinit var binding: ActivityNutriComparisionBinding
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<DietItem>
    private lateinit var db: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNutriComparisionBinding.inflate(layoutInflater)
        setContentView(binding.root)



        mAuth = FirebaseAuth.getInstance()



        newRecyclerView = findViewById(R.id.recycler_view1)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<DietItem>()
        getData()


        val intent=getIntent()
         val carb= intent?.getFloatExtra("carb_count",0f).toString()
        val pro= intent?.getFloatExtra("pro_count",0f).toString()
        val fat= intent?.getFloatExtra("fat_count",0f).toString()

        binding.carbYd.setText(carb)
        binding.proYd.setText(pro)
        binding.fatYd.setText(fat)




    }

    private fun getData() {

        val currentUser = mAuth.currentUser
        val uid = currentUser?.uid


//        for (i in 1..3) {
//
//            db.collection("Breakfast").document("$uid")
//                .get()
//                .addOnSuccessListener {
//                    val diet = DietItem(it['item'].toString(),)
//                    newArrayList.add(diet)
//                }
//            var adapter = DietItemAdapter(newArrayList)
//            newRecyclerView.adapter = adapter
//
//             //newRecyclerView.adapter = DietItemAdapter(newArrayList)
//        }


        db= FirebaseDatabase.getInstance().getReference(uid.toString()).child("Breakfast")
        db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){

                        val diet = userSnapshot.getValue(DietItem::class.java)
                        newArrayList.add(diet!!)

                    }

                    // searchArrayList.addAll(detailArrayList)

                    var adapter = DietItemAdapter(newArrayList)
                     newRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}