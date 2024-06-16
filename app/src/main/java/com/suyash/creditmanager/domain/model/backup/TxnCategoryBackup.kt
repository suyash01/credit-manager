package com.suyash.creditmanager.domain.model.backup

import com.google.gson.annotations.SerializedName
import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.util.TransactionType

data class TxnCategoryBackup(
    @SerializedName("type")
    val type: TransactionType,
    @SerializedName("name")
    val name: String
) {
    fun toTxnCategory() =
        TxnCategory(
            type = this.type,
            name = this.name
        )
}
