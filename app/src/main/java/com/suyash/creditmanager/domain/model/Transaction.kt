package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.suyash.creditmanager.domain.util.TransactionType
import java.time.LocalDate

@Entity(
    tableName = "transactions",
    indices = [Index(value = ["date"])]
)
@TypeConverters(Converters::class)
data class Transaction(
    @SerializedName("type")
    val type: TransactionType,
    @SerializedName("amount")
    val amount: Float,
    @SerializedName("card")
    val card: Int,
    @SerializedName("date")
    val date: LocalDate,
    @SerializedName("category")
    val category: String?,
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)
