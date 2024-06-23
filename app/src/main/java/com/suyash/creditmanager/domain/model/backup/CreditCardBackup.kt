package com.suyash.creditmanager.domain.model.backup

import com.google.gson.annotations.SerializedName
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.util.CardType

data class CreditCardBackup(
    @SerializedName("cardName")
    var cardName: String,
    @SerializedName("last4Digits")
    var last4Digits: String,
    @SerializedName("expiryDate")
    var expiryDate: String,
    @SerializedName("billDate")
    var billDate: Int,
    @SerializedName("gracePeriod")
    var gracePeriod: Boolean,
    @SerializedName("dueDate")
    var dueDate: Int,
    @SerializedName("cardType")
    var cardType: CardType,
    @SerializedName("limit")
    var limit: Int = 0,
    @SerializedName("bankName")
    var bankName: String?,
    @SerializedName("transactions")
    var transactions: List<TransactionBackup> = emptyList(),
    @SerializedName("emis")
    var emis: List<EmiBackup> = emptyList()
) {
    fun toCreditCard() =
        CreditCard(
            cardName = this.cardName,
            last4Digits = this.last4Digits,
            expiryDate = this.expiryDate,
            billDate = this.billDate,
            dueDate = this.dueDate,
            cardType = this.cardType,
            limit = this.limit,
            bankName = this.bankName,
            gracePeriod = this.gracePeriod
        )
}
