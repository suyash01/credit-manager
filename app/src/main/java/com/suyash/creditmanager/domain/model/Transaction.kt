package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.suyash.creditmanager.domain.model.backup.TransactionBackup
import com.suyash.creditmanager.domain.util.TransactionType
import java.time.LocalDate

@Entity(
    tableName = "transactions",
    indices = [Index(value = ["date"]), Index(value = ["card"])],
    foreignKeys = [
        ForeignKey(
            entity = CreditCard::class,
            parentColumns = ["id"],
            childColumns = ["card"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(Converters::class)
data class Transaction(
    val type: TransactionType,
    val amount: Float,
    val card: Int,
    val date: LocalDate,
    val category: String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
    fun toTransactionBackup() =
        TransactionBackup(
            type = this.type,
            amount = this.amount,
            date = this.date,
            category = this.category
        )
}
