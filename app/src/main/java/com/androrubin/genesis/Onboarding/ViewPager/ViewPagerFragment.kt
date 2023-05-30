package com.androrubin.genesis.Onboarding.ViewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.androrubin.genesis.Onboarding.*
import com.androrubin.genesis.R



class ViewPagerFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment(),
            FourthFragment(),
            FifthFragment()
        )

        val adapter =ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,lifecycle
        )
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter=adapter




        return view
    }


}