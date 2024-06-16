package com.suyash.creditmanager.domain.model.backup

import com.google.gson.annotations.SerializedName

data class DataBackup(
    @SerializedName("creditCards")
    var creditCards: List<CreditCardBackup> = emptyList(),
    @SerializedName("emis")
    var emis: List<EmiBackup> = emptyList(),
    @SerializedName("txnCategories")
    var txnCategories: List<TxnCategoryBackup> = emptyList()
)
