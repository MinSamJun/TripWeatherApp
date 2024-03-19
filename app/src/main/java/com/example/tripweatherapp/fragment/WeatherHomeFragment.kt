package com.example.tripweatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tripweatherapp.R
import com.example.tripweatherapp.databinding.FragmentWeatherHomeBinding
import com.example.tripweatherapp.extensions.showCitiesPopup
import com.example.tripweatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherHomeFragment : Fragment() {
    private var _binding: FragmentWeatherHomeBinding? = null
    private val binding get() = _binding!!
    private val currentDate by lazy { SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(Date()) }
    private val viewModel by activityViewModels<WeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeatherHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.regionText.observe(viewLifecycleOwner) { selectedCity ->
            binding.selectedRegionText.text = selectedCity
            fetchWeatherData() // 도시가 변경될 때마다 날씨 데이터 갱신
        }

        binding.selectRegionButton.setOnClickListener {
            showCitiesPopup(it, viewModel) { fetchWeatherData() }
        }

        binding.selectedRegionText.setOnClickListener {
            showCitiesPopup(it, viewModel) { fetchWeatherData() }
        }

        binding.selectRegionButton.setOnClickListener {
            showCitiesPopup(it, viewModel) { fetchWeatherData() }
        }

        viewModel.weatherData.observe(viewLifecycleOwner) { data ->
            with(binding) {
                mainWeatherText.text = data.skyStatus.text
                mainTemperTv.text = data.temperature
                mainRainTv.text = data.rainState.value.toString()
                mainWaterTv.text = data.humidity
                mainWindTv.text = data.windSpeed
                mainRainPercentTv.text = getString(R.string.rain_percent, data.rainPercent)
                rainStatusIv.setImageResource(data.rainState.icon)
                weatherStatusIv.setImageResource(data.skyStatus.colorIcon)
            }
        }
        fetchWeatherData()
    }



    fun fetchWeatherData() {
        // 현재 선택된 도시를 기반으로 날씨 데이터 요청
        viewModel.regionText.value?.let { city ->
            viewModel.getWeather(currentDate, city)
        }

    }
}