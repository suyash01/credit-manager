package com.suyash.creditmanager.domain.use_case

import com.suyash.creditmanager.domain.use_case.transaction.AddTransaction
import com.suyash.creditmanager.domain.use_case.transaction.DeleteTransaction
import com.suyash.creditmanager.domain.use_case.transaction.GetTransaction
import com.suyash.creditmanager.domain.use_case.transaction.GetTransactions

data class TransactionUseCase (
    val getTransactions: GetTransactions,
    val getTransaction: GetTransaction,
    val upsertTransaction: AddTransaction,
    val deleteTransaction: DeleteTransaction
)