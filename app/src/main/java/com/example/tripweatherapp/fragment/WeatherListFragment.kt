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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripweatherapp.R
import com.example.tripweatherapp.WeatherItemListAdapter
import com.example.tripweatherapp.data.cityList
import com.example.tripweatherapp.databinding.FragmentWeatherListBinding
import com.example.tripweatherapp.databinding.PopupListBinding
import com.example.tripweatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherListFragment : Fragment() {
    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private val listAdapter by lazy { WeatherItemListAdapter() }
    private val currentDate by lazy { SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(Date()) }
    private val viewModel by activityViewModels<WeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            weatherList.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(requireContext())
                DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                    getDrawable(requireContext(), R.drawable.list_divider)?.let { setDrawable(it) }
                }.also {
                    addItemDecoration(it)
                }
            }
            viewModel.regionText.observe(viewLifecycleOwner) {
                regionText.text = it
                fetchWeatherData()
            }
            viewModel.weatherList.observe(viewLifecycleOwner) {
                listAdapter.submitList(it)
                val currentWeather = it.first()
                weatherStatusIv.setImageResource(currentWeather.skyStatus.colorIcon)
                mainWeatherText.text = currentWeather.skyStatus.text
                mainTemperTv.text = currentWeather.temperature
            }
        }
        fetchWeatherData()

        binding.regionText.setOnClickListener {
            showCitiesPopup(it)
        }

        binding.regionButton.setOnClickListener {
            showCitiesPopup(it)
        }

    }

    private fun showCitiesPopup(anchorView: View) {
        val cities = cityList.map { it.name }.toList()

        val popupBinding = PopupListBinding.inflate(layoutInflater, null, false).also {
            it.cityList.adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
        }

        PopupWindow(popupBinding.root, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true).apply {
            setBackgroundDrawable(getDrawable(requireContext(), R.drawable.rectangle_background))
            elevation = 10f
            showAsDropDown(anchorView)
            popupBinding.cityList.setOnItemClickListener { _, _, position, _ ->
                val selectedCity = cities[position]
                viewModel.setRegion(selectedCity)
                // fetchWeatherData 호출로 날씨 데이터 갱신
                fetchWeatherData()
                dismiss()
            }

        }
    }

    private fun fetchWeatherData() {
        viewModel.getWeatherList(currentDate)
    }
}