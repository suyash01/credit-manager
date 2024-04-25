package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.suyash.creditmanager.domain.util.TransactionType
import java.time.LocalDate

@Entity(
    tableName = "transactions",
    indices = [Index(value = ["date"])]
)
@TypeConverters(Converters::class)
data class Transaction(
    val type: TransactionType,
    val amount: Float,
    val card: Int,
    val date: LocalDate,
    val category: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

class InvalidTransactionException(message: String): Exception(message)