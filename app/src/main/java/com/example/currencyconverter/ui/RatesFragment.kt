package com.example.currencyconverter.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyconverter.R
import com.example.currencyconverter.viewmodel.RatesViewModel
import kotlinx.android.synthetic.main.fragment_rates.*

class RatesFragment : Fragment(), RatesAdapter.OnRateInteraction {

    private var ratesAdapter: RatesAdapter? = null
    private var ratesViewModel: RatesViewModel? = null

    private var fragmentActive = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ratesViewModel = ViewModelProviders.of(activity!!).get(RatesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate (R.layout.fragment_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratesAdapter = RatesAdapter(this)
        ratesRecyclerView.adapter = ratesAdapter

        val mLayoutManager: RecyclerView.LayoutManager
        mLayoutManager = LinearLayoutManager(context)
        ratesRecyclerView.layoutManager = mLayoutManager

        val mDividerItemDecoration = DividerItemDecoration(
            ratesRecyclerView.getContext(),
            mLayoutManager.orientation
        )
        ratesRecyclerView.addItemDecoration(mDividerItemDecoration)

        observeModel()
    }

    private fun observeModel() {

        ratesViewModel?.getCurrencyRates()?.observe(this,
            Observer {currencyRates ->
                if (currencyRates != null) {
                    ratesAdapter?.updateList(currencyRates)
                }
            })
    }

    override fun onStart() {
        super.onStart()
        fragmentActive=true
        refreshThread().start()
    }

    fun refreshThread() = Thread {
        while (fragmentActive) {
            Thread.sleep(1000)
            ratesViewModel?.refreshRates()
        }
    }

    override fun onStop() {
        super.onStop()
        fragmentActive=false
    }

    override fun scrollToTop() {
        ratesRecyclerView.scrollToPosition(0)
    }

    override fun onRateChanged(currencyName: String, value: Float) {
        ratesViewModel?.setNewBase(currencyName,value)
    }

    override fun onValueChanged(value: Float) {
        ratesViewModel?.setNewBaseValue(value)
    }

    companion object {
        fun newInstance(): RatesFragment {
            val fragment = RatesFragment()
            return fragment
        }
    }
}