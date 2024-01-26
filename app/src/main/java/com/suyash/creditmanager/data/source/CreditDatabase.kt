package com.suyash.creditmanager.data.source

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.suyash.creditmanager.data.source.dao.CreditCardDao
import com.suyash.creditmanager.data.source.dao.EMIDao
import com.suyash.creditmanager.data.source.dao.TransactionDao
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.model.Transaction

@Database(
    entities = [CreditCard::class, Transaction::class, EMI::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class CreditDatabase: RoomDatabase() {

    abstract val creditCardDao: CreditCardDao
    abstract val transactionDao: TransactionDao
    abstract val emiDao: EMIDao

    companion object {
        const val DATABASE_NAME = "credit_db"
    }
}