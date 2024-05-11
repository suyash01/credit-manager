package com.suyash.creditmanager.domain.util

import com.google.gson.annotations.SerializedName

enum class TransactionType {
    @SerializedName("DEBIT")
    DEBIT,
    @SerializedName("CREDIT")
    CREDIT
}