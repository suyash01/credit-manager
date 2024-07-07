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

fun nextEmiDate(
    emiDate: LocalDate,
    months: Int,
    dateFormat: DateFormat
): String {
    var nextEmiDate = LocalDate.now().withDayOfMonth(emiDate.dayOfMonth)
    if (nextEmiDate.isBefore(LocalDate.now())) {
        nextEmiDate = nextEmiDate.plusMonths(1)
    }
    if (nextEmiDate.isBefore(emiDate.plusMonths(months - 1L))) {
        return formatDate(nextEmiDate, dateFormat)
    }
    return "Finished"
}

fun nextBillDate(
    billDate: Int,
    dateFormat: DateFormat
): String {
    var billingDate = LocalDate.now().withDayOfMonth(billDate)
    if (billingDate.isBefore(LocalDate.now())) {
        billingDate = billingDate.plusMonths(1)
    }
    return formatDate(billingDate, dateFormat)
}

fun nextDueDate(
    billDate: Int,
    dueDate: Int,
    gracePeriod: Boolean,
    dateFormat: DateFormat
): String {
    var paymentDate = LocalDate.now()
    paymentDate = if(gracePeriod) {
        paymentDate.withDayOfMonth(billDate).plusDays(dueDate.toLong())

    } else {
        paymentDate.withDayOfMonth(dueDate)
    }
    if(paymentDate.isBefore(LocalDate.now())) {
        paymentDate = paymentDate.plusMonths(1)
    }
    return formatDate(paymentDate, dateFormat)
}