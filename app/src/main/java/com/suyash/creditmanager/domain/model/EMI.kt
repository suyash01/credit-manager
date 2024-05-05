package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate

@Entity(
    tableName = "emis"
)
@TypeConverters(Converters::class)
data class EMI(
    val name: String,
    val amount: Float,
    val rate: Float,
    val months: Int,
    val card: Int?,
    val date: LocalDate,
    val taxRate: Float?,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)

class InvalidEMIException(message: String): Exception(message)
