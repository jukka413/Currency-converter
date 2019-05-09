package com.example.currencyconverter.ui

import android.support.v7.util.DiffUtil
import com.example.currencyconverter.viewmodel.model.CurrencyRate

class RatesDiff(private val oldList: List<CurrencyRate>, private val newList: List<CurrencyRate>) : DiffUtil.Callback()  {
    companion object {
        const val VALUE_CHG = "VALUE_CHG"
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].currency == newList[newItemPosition].currency
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldRate = oldList[oldItemPosition]
        val newRate = newList[newItemPosition]

        val payloadSet = mutableSetOf<String>()

        if (oldRate.value!=newRate.value)
            payloadSet.add(VALUE_CHG)

        return payloadSet
    }

}