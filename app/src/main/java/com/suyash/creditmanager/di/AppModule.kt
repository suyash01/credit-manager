package com.suyash.creditmanager.di

import android.app.Application
import androidx.room.Room
import com.suyash.creditmanager.data.repository.CreditCardRepositoryImpl
import com.suyash.creditmanager.data.source.CreditCardDatabase
import com.suyash.creditmanager.domain.repository.CreditCardRepository
import com.suyash.creditmanager.domain.use_case.AddCreditCard
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.DeleteCreditCard
import com.suyash.creditmanager.domain.use_case.GetCreditCard
import com.suyash.creditmanager.domain.use_case.GetCreditCards
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCreditCardDatabase(app: Application): CreditCardDatabase {
        return Room.databaseBuilder(app, CreditCardDatabase::class.java, CreditCardDatabase.DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideCreditCardRepository(db: CreditCardDatabase): CreditCardRepository {
        return CreditCardRepositoryImpl(db.creditCardDao)
    }

    @Provides
    @Singleton
    fun provideCreditCardUseCases(repository: CreditCardRepository): CreditCardUseCases {
        return CreditCardUseCases(
            getCreditCards = GetCreditCards(repository),
            getCreditCard = GetCreditCard(repository),
            upsertCreditCard = AddCreditCard(repository),
            deleteCreditCard = DeleteCreditCard(repository)
        )
    }
}