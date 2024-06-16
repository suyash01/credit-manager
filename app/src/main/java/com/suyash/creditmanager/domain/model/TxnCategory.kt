package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.suyash.creditmanager.domain.model.backup.TxnCategoryBackup
import com.suyash.creditmanager.domain.util.TransactionType

@Entity(
    tableName = "txn_categories"
)
data class TxnCategory(
    val type: TransactionType,
    val name: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
    fun toTxnCategoryBackup() =
        TxnCategoryBackup(
            type = this.type,
            name = this.name
        )
}
