package com.suyash.creditmanager.presentation.util

import java.text.NumberFormat
import java.util.Locale

class CCUtils {
    companion object {
        fun expiryDateMask(expiryDate: String): String {
            var out = ""
            for (i in expiryDate.indices) {
                out += expiryDate[i]
                if (i == 1) out += "/"
            }
            return out
        }

        fun currencyMask(amount: Int): String {
            return NumberFormat.getCurrencyInstance(Locale("hi", "IN")).format(amount)
        }
    }
}