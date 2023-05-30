package com.androrubin.genesis.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androrubin.genesis.DietPlanning
import com.androrubin.genesis.chatBot.ChatBotActivity
import com.androrubin.genesis.databinding.FragmentHomeBinding
import com.androrubin.genesis.journaling.GetJournal
import com.androrubin.genesis.ui.home.trackers.BreastFeedingTrackerActivity
import com.androrubin.genesis.yoga_and_meditation.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private  lateinit var mAuth : FirebaseAuth
    private  lateinit var db : FirebaseFirestore
    private  var progressBar: Int = 0

   // private lateinit var activity: CreateProfile

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        progressBar = 0
        db= FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val name = currentUser?.displayName
        val uid = currentUser?.uid
        var week_count=""


        db.collection("Users").document("$uid")
            .get()
            .addOnSuccessListener  {
                   week_count = it["Week Count"].toString()
                Toast.makeText(context," $week_count",Toast.LENGTH_LONG).show()

                if (week_count != "") {

                    val week_val=week_count.toInt()
                    if(week_val<=39)
                        progressBar+= (week_val?.times(2.5))!!.toInt()
                    else
                    {
                        progressBar=100
                        Toast.makeText(context," Your delivery ",Toast.LENGTH_SHORT).show()

                    }

                    Toast.makeText(context," $progressBar",Toast.LENGTH_SHORT).show()

                    binding.imgBaby.progress=progressBar
                }

                }

        binding.cardJourn1.setOnClickListener {
            val intent = Intent(context,GetJournal::class.java)
            startActivity(intent)
        }

        binding.imageButton.setOnClickListener {
            startActivity(Intent(context, ChatBotActivity::class.java))
        }




        binding.cardDiet1.setOnClickListener {

            val intent = Intent(context,DietPlanning::class.java)
            startActivity(intent)

        }
        binding.imgVac.setOnClickListener {
            startActivity(Intent(context, Exercise::class.java))
        }
        return root
    }





}


