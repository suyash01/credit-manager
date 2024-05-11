package com.suyash.creditmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.suyash.creditmanager.domain.util.TransactionType

@Entity(
    tableName = "txn_categories"
)
data class TxnCategory(
    @SerializedName("type")
    val type: TransactionType,
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)
