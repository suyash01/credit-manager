package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.suyash.creditmanager.domain.util.CardType

@Entity(tableName = "credit_cards")
data class CreditCard(
    @SerializedName("cardName")
    var cardName: String,
    @SerializedName("last4Digits")
    var last4Digits: String,
    @SerializedName("expiryDate")
    var expiryDate: String,
    @SerializedName("billDate")
    var billDate: Int,
    @SerializedName("dueDate")
    var dueDate: Int,
    @SerializedName("cardType")
    var cardType: CardType,
    @SerializedName("limit")
    var limit: Int = 0,
    @SerializedName("bankName")
    var bankName: String?,
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)

class InvalidCreditCardException(message: String): Exception(message)
