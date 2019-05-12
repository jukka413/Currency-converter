package com.example.currencyconverter.ui

import android.support.v7.widget.RecyclerView
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.text.TextWatcher
import com.example.currencyconverter.R
import com.example.currencyconverter.viewmodel.model.CurrencyRate

class RateViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    val rateLayout: View = this.itemView.findViewById(R.id.rateLayout)
    private val currencyName: TextView = this.itemView.findViewById(R.id.currencyNameTextView)
    private val currencyImage: ImageView = this.itemView.findViewById(R.id.currencyImageView)
    private val currencyDesc: TextView = this.itemView.findViewById(R.id.currencyDescTextView)
    private val currencyRateEdit: EditText = this.itemView.findViewById(R.id.rateEditText)

    fun bindTo(currencyRate: CurrencyRate, position: Int, valueWatcher: TextWatcher) {

        currencyRateEdit.isEnabled = position == 0

        currencyRateEdit.removeTextChangedListener(valueWatcher)
        if (position==0)
            currencyRateEdit.addTextChangedListener(valueWatcher)

        updateValue(currencyRate,position)
        updateCurrency(currencyRate,position)
    }
    fun updateValue(currencyRate: CurrencyRate, position: Int) {

        val newValue: String = "%.2f".format(currencyRate.value)

        if (currencyRateEdit.text.toString() != newValue) {
            currencyRateEdit.setText(newValue)
        }
    }
    private fun updateCurrency(currencyRate: CurrencyRate, position: Int) {

        val updatedCurrency = currencyRate.currency

        currencyName.text = updatedCurrency
        currencyDesc.text = getCurrencyDesc(updatedCurrency)

        val flagDrawable = ResourcesCompat.getDrawable(currencyImage.resources, getCurrencyFlag(updatedCurrency), null)
        currencyImage.setImageDrawable(flagDrawable)

    }
    private fun getCurrencyFlag(currencyName: String): Int {
        var currencyFlag: Int

        val resources = currencyImage.context.resources
        val packageName = currencyImage.context.packageName
        val drawableName = "ic_"+currencyName.substring(0,2).toLowerCase()

        currencyFlag = try {
            resources.getIdentifier(drawableName, "drawable", packageName)
        } catch (e: Exception) { 0 }

        if (currencyFlag==0)
            currencyFlag = R.drawable.ic_cat

        return currencyFlag
    }

    private fun getCurrencyDesc(currencyName: String): String {
        val currencyDescName: String

        val resources = currencyDesc.context.resources
        val packageName = currencyDesc.context.packageName

        currencyDescName= try {
            resources.getString(resources.getIdentifier(currencyName, "string", packageName))
        } catch (e: Exception) {
            "Err"
        }
        return currencyDescName
    }
}