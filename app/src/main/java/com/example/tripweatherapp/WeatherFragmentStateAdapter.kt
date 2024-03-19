package com.example.tripweatherapp

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tripweatherapp.fragment.WeatherHomeFragment
import com.example.tripweatherapp.fragment.WeatherListFragment

class WeatherFragmentStateAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WeatherHomeFragment()
            else -> WeatherListFragment()
        }
    }
}