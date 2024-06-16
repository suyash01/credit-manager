package com.suyash.creditmanager.domain.model.backup

import com.google.gson.annotations.SerializedName
import com.suyash.creditmanager.domain.model.EMI
import java.time.LocalDate

data class EmiBackup(
    @SerializedName("name")
    val name: String,
    @SerializedName("amount")
    val amount: Float,
    @SerializedName("rate")
    val rate: Float,
    @SerializedName("months")
    val months: Int,
    @SerializedName("date")
    val date: LocalDate,
    @SerializedName("taxRate")
    val taxRate: Float?
) {
    fun toEmi(card: Int?) =
        EMI (
            name = this.name,
            amount = this.amount,
            rate = this.rate,
            months = this.months,
            date = this.date,
            taxRate = this.taxRate,
            card = card
        )
}
