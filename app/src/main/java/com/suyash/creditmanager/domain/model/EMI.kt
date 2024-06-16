package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.suyash.creditmanager.domain.model.backup.EmiBackup
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.min

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
) {
    fun emisPaid(): Int {
        if (date.isAfter(LocalDate.now())) {
            return 0
        }
        val emiPaid: Int = ChronoUnit.MONTHS.between(date, LocalDate.now()).toInt() + 1
        return min(emiPaid, months)
    }

    fun toEmiBackup() =
        EmiBackup (
            name = this.name,
            amount = this.amount,
            rate = this.rate,
            months = this.months,
            date = this.date,
            taxRate = this.taxRate
        )
}
