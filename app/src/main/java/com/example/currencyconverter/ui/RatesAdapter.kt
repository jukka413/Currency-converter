package com.example.currencyconverter.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyconverter.viewmodel.model.CurrencyRate
import android.os.Handler
import com.example.currencyconverter.R
import java.util.*


class RatesAdapter(private val callback: OnRateInteraction) : RecyclerView.Adapter<RateViewHolder>() {

    private val handler:Handler = Handler()
    private val valueWatcher: TextWatcher
    private var ratesList: List<CurrencyRate>? = null


    companion object {
        const val ROOT_RATE = 0
        const val OTHER_RATE = 1
    }

    interface OnRateInteraction {
        fun onRateChanged(currencyName: String, value: Float)
        fun onValueChanged(value: Float)
        fun scrollToTop()
    }

    init {

        this.valueWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(newValue: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(newValue: Editable?) {
                val strValue: String = newValue.toString().trim()
                var value: Float

                value = try {
                    strValue.toFloat()
                } catch (e: Exception) {
                    0F
                }

                ratesList!![0].value = value
                callback.onValueChanged(value)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RateViewHolder {

        val view: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.rate, p0, false)

        val rateHolder = RateViewHolder(view)

        rateHolder.rateLayout.setOnClickListener {
            val pos: Int = rateHolder .adapterPosition
            if (pos != RecyclerView.NO_POSITION && pos>0) {
                callback.onRateChanged(ratesList!![pos].currency, ratesList!![pos].value)
            }
        }

        return rateHolder
    }

    override fun onBindViewHolder(p0: RateViewHolder, position: Int) {
        p0.bindTo(ratesList!![position],position,valueWatcher)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int, payloads: MutableList<Any>) {

        val set = payloads.firstOrNull() as Set<String>?

        if (set==null || set.isEmpty() ) {
            return super.onBindViewHolder(holder, position, payloads)
        }

        if (set.contains(RatesDiff.VALUE_CHG)){
            holder.updateValue(ratesList!![position],position)
        }
    }

    override fun getItemCount(): Int {
        return if (ratesList==null) 0 else ratesList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==0) ROOT_RATE else OTHER_RATE
    }

    private val pendingList: Deque<List<CurrencyRate>> = LinkedList()

    fun updateList(newRatesList: List<CurrencyRate>) {
        if (ratesList == null) {
            ratesList = newRatesList
            notifyItemRangeInserted(0, newRatesList.size)
            return
        }

        pendingList.push(newRatesList)

        if (pendingList.size>1)
            return

        calculateDiff(newRatesList)
    }

    private fun calculateDiff(latest: List<CurrencyRate>) {
        Thread {

            val diffResult = DiffUtil.calculateDiff(RatesDiff(ratesList!!, latest))

            val newRootRate: Boolean = (ratesList!![0].currency!= latest[0].currency)
            ratesList = latest

            pendingList.remove(latest)

            handler.post {
                diffResult.dispatchUpdatesTo(this)
                if (newRootRate)
                    callback.scrollToTop()
            }

            if (pendingList.size>0){
                calculateDiff(pendingList. pop())
                pendingList.clear()
            }
        }.start()
    }
}