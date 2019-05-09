package com.example.currencyconverter.api

import com.example.currencyconverter.api.model.LatestRates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("latest")
    fun getLatestRates(@Query("base") baseRate: String): Call<LatestRates>

}