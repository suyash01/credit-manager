package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.suyash.creditmanager.domain.model.backup.CreditCardBackup
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
    var bankName: String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
    fun toCreditCardBackup() =
        CreditCardBackup(
            cardName = this.cardName,
            last4Digits = this.last4Digits,
            expiryDate = this.expiryDate,
            billDate = this.billDate,
            dueDate = this.dueDate,
            cardType = this.cardType,
            limit = this.limit,
            bankName = this.bankName
        )
}

class InvalidCreditCardException(message: String): Exception(message)
