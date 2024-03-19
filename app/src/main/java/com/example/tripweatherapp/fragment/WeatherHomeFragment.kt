package com.example.tripweatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tripweatherapp.R
import com.example.tripweatherapp.data.cityList
import com.example.tripweatherapp.databinding.FragmentWeatherHomeBinding
import com.example.tripweatherapp.databinding.PopupListBinding
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
            showCitiesPopup(it)
        }

        binding.selectedRegionText.setOnClickListener {
            showCitiesPopup(it)
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

    private fun showCitiesPopup(anchorView: View) {
        val cities = cityList.map { it.name }.toList()

        val popupBinding = PopupListBinding.inflate(layoutInflater, null, false).also {
            it.cityList.adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
        }

        viewModel.regionText.observe(viewLifecycleOwner) { selectedCity ->
            binding.selectedRegionText.text = selectedCity
        }

        PopupWindow(popupBinding.root, 500, LinearLayout.LayoutParams.WRAP_CONTENT, true).apply {
            setBackgroundDrawable(getDrawable(requireContext(), R.drawable.rectangle_background))
            elevation = 10f
            showAsDropDown(anchorView)

            popupBinding.cityList.setOnItemClickListener { _, _, position, _ ->
                val selectedCity = cities[position]
                binding.selectedRegionText.text = selectedCity
                viewModel.setRegion(selectedCity)
//                fetchWeatherData(selectedCity)
                fetchWeatherData()
                dismiss()
            }
        }
    }

//    private fun fetchWeatherData(city: String = "서울특별시") {
//        viewModel.getWeather(currentDate, city)
//    }

    private fun fetchWeatherData() {
        // 현재 선택된 도시를 기반으로 날씨 데이터 요청
        viewModel.regionText.value?.let { city ->
            viewModel.getWeather(currentDate, city)
        }
    }

}