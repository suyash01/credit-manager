package com.suyash.creditmanager.presentation.commons

import com.suyash.creditmanager.domain.util.DateFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale

class CMUtils {
    companion object {
        // TODO: remove this and use the more generic one below
        fun currencyMask(amount: Float, countryCode: String): String {
            return NumberFormat.getCurrencyInstance().apply {
                currency = Currency.getInstance(Locale("", countryCode))
                maximumFractionDigits = 2
            }.format(amount)
        }

        fun formatDate(dateTime: LocalDate, dateFormat: DateFormat): String {
            return dateTime.format(DateTimeFormatter.ofPattern(dateFormat.format))
        }
    }
}

fun formatCurrencyAmount(
    amount: Float,
    fractionDigits: Int = 2,
    countryCode: String,
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