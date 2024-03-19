package com.example.tripweatherapp.extensions

import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import com.example.tripweatherapp.R
import com.example.tripweatherapp.data.cityList
import com.example.tripweatherapp.databinding.PopupListBinding
import com.example.tripweatherapp.viewmodel.WeatherViewModel

fun Fragment.showCitiesPopup(anchorView: View, viewModel: WeatherViewModel, fetchWeatherDataCallback: () -> Unit)
 {
    val cities = cityList.map { it.name }.toList()

    val popupBinding = PopupListBinding.inflate(layoutInflater, null, false).also {
        it.cityList.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
    }

    PopupWindow(popupBinding.root, 500, LinearLayout.LayoutParams.WRAP_CONTENT, true).apply {
        setBackgroundDrawable(getDrawable(requireContext(), R.drawable.rectangle_background))
        elevation = 10f
        showAsDropDown(anchorView)

        popupBinding.cityList.setOnItemClickListener { _, _, position, _ ->
            val selectedCity = cities[position]
            viewModel.setRegion(selectedCity)
            fetchWeatherDataCallback()
            dismiss()
        }
    }
}