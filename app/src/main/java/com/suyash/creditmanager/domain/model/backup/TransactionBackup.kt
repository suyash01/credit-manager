package com.suyash.creditmanager.domain.model.backup

import com.google.gson.annotations.SerializedName
import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.util.TransactionType
import java.time.LocalDate

data class TransactionBackup(
    @SerializedName("type")
    val type: TransactionType,
    @SerializedName("amount")
    val amount: Float,
    @SerializedName("date")
    val date: LocalDate,
    @SerializedName("category")
    val category: String?
) {
    fun toTransaction(card: Int) =
        Transaction(
            type = this.type,
            amount = this.amount,
            date = this.date,
            category = this.category,
            card = card
        )
}
