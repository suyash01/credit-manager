package com.suyash.creditmanager.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suyash.creditmanager.data.source.CreditCardDao
import com.suyash.creditmanager.domain.model.CreditCard

@Database(
    entities = [CreditCard::class],
    version = 1
)
abstract class CreditCardDatabase: RoomDatabase() {

    abstract val creditCardDao: CreditCardDao;

    companion object {
        const val DATABASE_NAME = "credit_card_db"
    }
}