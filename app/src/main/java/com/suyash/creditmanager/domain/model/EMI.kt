package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

@Entity(
    tableName = "emis"
)
@TypeConverters(Converters::class)
data class EMI(
    @SerializedName("name")
    val name: String,
    @SerializedName("amount")
    val amount: Float,
    @SerializedName("rate")
    val rate: Float,
    @SerializedName("months")
    val months: Int,
    @SerializedName("card")
    val card: Int?,
    @SerializedName("date")
    val date: LocalDate,
    @SerializedName("taxRate")
    val taxRate: Float?,
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)
