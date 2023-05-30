package com.androrubin.genesis.Onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.androrubin.genesis.R


class FifthFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_fifth, container, false)


        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        val next5 = view.findViewById<ImageView>(R.id.next5)
        next5.setOnClickListener {

            findNavController().navigate(R.id.action_viewPagerFragment_to_loginActivity)
            onBoardingFinished()

        }
        val prev = view.findViewById<TextView>(R.id.prev)
        prev.setOnClickListener {
            viewPager?.currentItem=3

        }

        return view
    }

    private fun onBoardingFinished(){
        val setsharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = setsharedPref.edit()
        editor.putBoolean("Finished",true)
        editor.apply()
    }


}