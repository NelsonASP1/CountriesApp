package com.nasp.countriesapp.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Float.toReadable(): String = DecimalFormat("###,###.##").apply {
    roundingMode = RoundingMode.FLOOR
    decimalFormatSymbols = DecimalFormatSymbols().apply {
        decimalSeparator = '.'
        groupingSeparator = ' '
    }
}.format(this)

fun Float.toTwoDecimals(): Float = DecimalFormat("#.##").apply {
    roundingMode = RoundingMode.FLOOR
    decimalFormatSymbols = DecimalFormatSymbols().apply {
        decimalSeparator = '.'
        groupingSeparator = ' '
    }
}.format(this).toFloat()