package com.suyash.creditmanager.presentation.util

import com.suyash.creditmanager.domain.util.DateFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CMUtils {
    companion object {
        fun expiryDateMask(expiryDate: String): String {
            var out = ""
            for (i in expiryDate.indices) {
                out += expiryDate[i]
                if (i == 1) out += "/"
            }
            return out
        }

        fun currencyMask(amount: Float, countryCode: String): String {
            return NumberFormat.getCurrencyInstance(Locale("", countryCode)).format(amount)
        }

        fun formatDate(dateTime: LocalDate, dateFormat: DateFormat): String {
            return dateTime.format(DateTimeFormatter.ofPattern(dateFormat.format))
        }
    }
}