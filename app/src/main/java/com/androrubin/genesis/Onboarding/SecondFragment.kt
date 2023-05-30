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



class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_second, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        val next2 = view.findViewById<ImageView>(R.id.next2)
        val prev = view.findViewById<TextView>(R.id.prev)

        next2.setOnClickListener {
            viewPager?.currentItem=2

        }
        prev.setOnClickListener {
            viewPager?.currentItem=0

        }
        return view
    }


}