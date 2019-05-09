package com.example.currencyconverter.viewmodel

import android.arch.lifecycle.ViewModel
import com.example.currencyconverter.api.ApiClient
import com.example.currencyconverter.api.model.LatestRates
import com.example.currencyconverter.viewmodel.model.CurrencyRate
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer


class RatesViewModel: ViewModel()  {
    private val RATE_UPDATE: Any = Object()

    // Default values
    private var baseCurrency: String = "EUR"
    private var baseValue: Float = 100F

    private val latestRatesObs: Observer<LatestRates>;
    private val currencyRates: MutableLiveData<List<CurrencyRate>> = MutableLiveData()


    init {

        latestRatesObs = Observer {
            if (it != null) updateLatestRates(it)
        }

        ApiClient.getLatestRates().observeForever(latestRatesObs)
    }

    override fun onCleared() {
        ApiClient.getLatestRates().removeObserver(latestRatesObs)
    }

    private fun updateLatestRates(latestRates: LatestRates) {

        val newCurrencyRates: MutableList<CurrencyRate> = mutableListOf<CurrencyRate>()

        synchronized(RATE_UPDATE) {

            newCurrencyRates.add(CurrencyRate(latestRates.base!!, 1.0F, baseValue))

            latestRates.rates?.forEach { (currency, rate) ->
                newCurrencyRates.add(CurrencyRate(currency, rate, rate*baseValue))
            }

            currencyRates.value = newCurrencyRates
        }
    }

    fun getCurrencyRates() : LiveData<List<CurrencyRate>> = currencyRates

    fun refreshRates() {
        ApiClient.refreshLatestRates(baseCurrency);
    }

    fun setNewBase(newBaseCurrency: String, newBaseValue: Float) {

        // Ignore if same
        if (baseCurrency.equals(newBaseCurrency))
            return;

        baseCurrency = newBaseCurrency
        baseValue = newBaseValue
        refreshRates()
    }

    fun setNewBaseValue(value: Float) {
        // Ignore so it doesn't enter an infinite loop
        if (baseValue.equals(value))
            return

        synchronized(RATE_UPDATE) {
            baseValue = value

            val newCurrencyRates: MutableList<CurrencyRate> = mutableListOf<CurrencyRate>()

            currencyRates.value?.forEach { newCurrencyRates.add(CurrencyRate(it.currency,it.rate,it.rate*baseValue))}
            currencyRates.value = newCurrencyRates
        }
    }
}