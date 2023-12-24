package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.suyash.creditmanager.domain.util.TransactionType
import java.time.LocalDate

@Entity(tableName = "transactions")
data class Transaction(
    val type: TransactionType,
    val amount: Float,
    val card: Int,
    val date: LocalDate,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)

class InvalidTransactionException(message: String): Exception(message)