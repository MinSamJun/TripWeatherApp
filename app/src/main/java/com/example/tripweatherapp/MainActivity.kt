package com.example.tripweatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tripweatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            val adapter = WeatherFragmentStateAdapter(this@MainActivity)
            viewPager.adapter = adapter
            indicator.setViewPager(viewPager)
            adapter.registerAdapterDataObserver(indicator.adapterDataObserver)

//            viewPager.currentItem = 1
        }
    }
}