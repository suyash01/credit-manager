package com.suyash.creditmanager.presentation.commons

import com.suyash.creditmanager.domain.util.DateFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale

fun formatDate(dateTime: LocalDate, dateFormat: DateFormat): String {
    return dateTime.format(DateTimeFormatter.ofPattern(dateFormat.format))
}

fun formatCurrencyAmount(
    amount: Float,
    countryCode: String,
    fractionDigits: Int = 2,
    currencySymbol: Boolean = true
): String {
    val locale = Locale("", countryCode)
    return if (currencySymbol) {
        NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(locale)
            maximumFractionDigits = fractionDigits
        }.format(amount)
    } else {
        NumberFormat.getInstance(locale).apply {
            maximumFractionDigits = fractionDigits
        }.format(amount)
    }
}