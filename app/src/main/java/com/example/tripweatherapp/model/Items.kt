package com.example.tripweatherapp.model


import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("item")
    val item: List<Item>
)