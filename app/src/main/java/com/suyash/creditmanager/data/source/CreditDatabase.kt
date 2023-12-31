package com.suyash.creditmanager.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suyash.creditmanager.data.source.dao.CreditCardDao
import com.suyash.creditmanager.data.source.dao.TransactionDao
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.Transaction

@Database(
    entities = [CreditCard::class, Transaction::class],
    version = 1,
    exportSchema = true
)
abstract class CreditDatabase: RoomDatabase() {

    abstract val creditCardDao: CreditCardDao
    abstract val transactionDao: TransactionDao

    companion object {
        const val DATABASE_NAME = "credit_db"
    }
}