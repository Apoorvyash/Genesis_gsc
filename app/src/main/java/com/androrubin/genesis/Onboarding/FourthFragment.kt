package com.androrubin.genesis.Onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.androrubin.genesis.R




class FourthFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fourth, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        val next4= view.findViewById<ImageView>(R.id.next4)
        val prev = view.findViewById<TextView>(R.id.prev)
        next4.setOnClickListener {
            viewPager?.currentItem=4

        }
        prev.setOnClickListener {
            viewPager?.currentItem=2
        }

        return view
    }


}