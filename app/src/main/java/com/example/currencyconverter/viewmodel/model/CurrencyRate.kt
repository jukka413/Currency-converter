package com.example.currencyconverter.viewmodel.model

data class CurrencyRate (
    val currency: String,
    val rate: Float,
    var value : Float
)