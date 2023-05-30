package com.androrubin.genesis.yoga_and_meditation


import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.androrubin.genesis.databinding.ActivityExerciseBinding
import com.apoorv.runanddetect.yogaadapter
import com.apoorv.runanddetect.yogacModel
import java.util.ArrayList

class Exercise : AppCompatActivity() , OnClickItemListener{
    private lateinit var binding:ActivityExerciseBinding
    lateinit var yogaName:Array<String>
    lateinit var yogaDetails:Array<String>
    private lateinit var newArrayList:ArrayList<yogacModel>
    private lateinit var myRecyclerView: RecyclerView
    private var postList = ArrayList<yogacModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myRecyclerView=findViewById<RecyclerView>(R.id.card_recycler)
        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.setHasFixedSize(true)
        getUserData()


    }
    fun getUserData() {

        val posterName = arrayOf(
            "Bhadrasana",
            "Trikonasana",
            "Upavistha Konasana",
            "Uttanasana",
            "Uthanasana",
            "Vajrasana",
            "Veerbhadrasana",
            "Viprita kirani",
            "Marjaryasana",
            "Balasana"
        )
        val Images=intArrayOf(
            R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,
            R.drawable.e,R.drawable.f,R.drawable.g,
            R.drawable.h,R.drawable.i,R.drawable.j)

        val description = arrayOf(
            "Bhadrasana is considered as the 4th Asana appropriate for extended periods of sitting. It says that a yogi can get rid of fatigue by sitting in this Asana.",
            "The modified triangle pose yoga during pregnancy helps women to restore their balance since their centre of gravity frequently changes and weakens over time.",
            "This pose increases flexibility in your low back, hips, and legs. It builds strength in your spine, low back, and pelvis.",
            "It gives us a chance to “look inward'“, both physically and metaphorically. On a physically level, this pose helps to stretch the hamstrings and calves.",
            "This pose relieves tension and promotes inner calm.",
            "Good exercise to the legs and thighs. This asana can be done throughout pregnancy.",
            "The prenatal warrior pose is considered safe during the second and third trimesters of pregnancy. This pose is known to strengthen your legs",
            "This posture is good for low back pain, revitalizing the body, and combating nausea.",
            "This gentle backbend relieves tension, improves spinal mobility, and boosts circulation. You’ll feel a nice stretch in your neck, shoulders, and torso.",
            "This relaxing pose stretches your shoulders, chest, and low back. It increases flexibility in your spine, hips, and thighs."
        )

        for (i in 0..posterName.size-1) {
            val post = yogacModel(posterName[i], description[i], Images[i])
            postList.add(post)
        }
        val adapter = yogaadapter(postList, this)
        myRecyclerView.layoutManager=LinearLayoutManager(this)

        myRecyclerView.adapter=adapter
//        adapter.setOnItemClickListener(object : yogaadapter.onItemClickListener{
//            override fun onItemCLick(position: Int){
//                val intent = Intent(this@Exercise, YogaDesc::class.java)
//                intent.putExtra("header", postList[position].yogaName)
//                intent.putExtra("details", postList[position].yogaDetails)
//                startActivity(intent)
//            }
//        })
    }

    override fun onMovieItemClicked(position: Int) {
        val intent= Intent(this, YogaDesc::class.java)
        intent.putExtra("name", postList[position].yogaName)
        intent.putExtra("Detail", postList[position].yogaDetails)
        intent.putExtra("ImageId", postList[position].imgId)
        startActivity(intent)
    }
}
