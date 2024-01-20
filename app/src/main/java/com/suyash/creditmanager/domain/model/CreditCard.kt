package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.suyash.creditmanager.domain.util.CardType

@Entity(tableName = "credit_cards")
data class CreditCard(
    var cardName: String,
    var last4Digits: String,
    var expiryDate: String,
    var billDate: Int,
    var dueDate: Int,
    var cardType: CardType,
    var limit: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

class InvalidCreditCardException(message: String): Exception(message)
