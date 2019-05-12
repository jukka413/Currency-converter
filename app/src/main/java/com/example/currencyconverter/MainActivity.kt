package com.example.currencyconverter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.arch.lifecycle.ViewModelProviders
import com.example.currencyconverter.ui.RatesFragment
import com.example.currencyconverter.viewmodel.RatesViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Locale.setDefault(Locale.US)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ratesViewModel: RatesViewModel = ViewModelProviders.of(this).get(RatesViewModel::class.java)
        ratesViewModel.refreshRates()

        displayFragmentRates()
    }

    private fun displayFragmentRates() {
        Locale.setDefault(Locale.US)
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()

        fragmentTransaction.replace(R.id.mainFrameLayout, RatesFragment.newInstance())
        fragmentTransaction.commit()
    }

}
