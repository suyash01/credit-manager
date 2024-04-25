package com.suyash.creditmanager.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.suyash.creditmanager.data.repository.CreditCardRepositoryImpl
import com.suyash.creditmanager.data.repository.EMIRepositoryImpl
import com.suyash.creditmanager.data.repository.TransactionRepositoryImpl
import com.suyash.creditmanager.data.repository.TxnCategoryRepositoryImpl
import com.suyash.creditmanager.data.settings.AppSettings
import com.suyash.creditmanager.data.settings.AppSettingsSerializer
import com.suyash.creditmanager.data.source.CreditDatabase
import com.suyash.creditmanager.domain.repository.CreditCardRepository
import com.suyash.creditmanager.domain.repository.EMIRepository
import com.suyash.creditmanager.domain.repository.TransactionRepository
import com.suyash.creditmanager.domain.repository.TxnCategoryRepository
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.EMIUseCases
import com.suyash.creditmanager.domain.use_case.TransactionUseCase
import com.suyash.creditmanager.domain.use_case.TxnCategoryUseCase
import com.suyash.creditmanager.domain.use_case.credit_card.AddCreditCard
import com.suyash.creditmanager.domain.use_case.credit_card.DeleteCreditCard
import com.suyash.creditmanager.domain.use_case.credit_card.GetCreditCard
import com.suyash.creditmanager.domain.use_case.credit_card.GetCreditCards
import com.suyash.creditmanager.domain.use_case.emi.AddEMI
import com.suyash.creditmanager.domain.use_case.emi.DeleteEMI
import com.suyash.creditmanager.domain.use_case.emi.GetEMI
import com.suyash.creditmanager.domain.use_case.emi.GetEMIs
import com.suyash.creditmanager.domain.use_case.transaction.AddTransaction
import com.suyash.creditmanager.domain.use_case.transaction.DeleteTransaction
import com.suyash.creditmanager.domain.use_case.transaction.GetTransaction
import com.suyash.creditmanager.domain.use_case.transaction.GetTransactions
import com.suyash.creditmanager.domain.use_case.txn_category.AddTxnCategory
import com.suyash.creditmanager.domain.use_case.txn_category.DeleteTxnCategory
import com.suyash.creditmanager.domain.use_case.txn_category.GetTxnCategories
import com.suyash.creditmanager.domain.use_case.txn_category.GetTxnCategory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCreditCardDatabase(app: Application): CreditDatabase {
        return Room
            .databaseBuilder(app, CreditDatabase::class.java, CreditDatabase.DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideCreditCardRepository(db: CreditDatabase): CreditCardRepository {
        db.openHelper.writableDatabase
        return CreditCardRepositoryImpl(db.creditCardDao)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(db: CreditDatabase): TransactionRepository {
        return TransactionRepositoryImpl(db.transactionDao)
    }

    @Provides
    @Singleton
    fun provideEMIRepository(db: CreditDatabase): EMIRepository {
        return EMIRepositoryImpl(db.emiDao)
    }

    @Provides
    @Singleton
    fun provideTxnCategoryRepository(db: CreditDatabase): TxnCategoryRepository {
        return TxnCategoryRepositoryImpl(db.txnCategoryDao)
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

    @Provides
    @Singleton
    fun provideTransactionUseCases(repository: TransactionRepository): TransactionUseCase {
        return TransactionUseCase(
            getTransactions = GetTransactions(repository),
            getTransaction = GetTransaction(repository),
            upsertTransaction = AddTransaction(repository),
            deleteTransaction = DeleteTransaction(repository)
        )
    }

    @Provides
    @Singleton
    fun provideEMIUseCases(repository: EMIRepository): EMIUseCases {
        return EMIUseCases(
            getEMIs = GetEMIs(repository),
            getEMI = GetEMI(repository),
            upsertEMI = AddEMI(repository),
            deleteEMI = DeleteEMI(repository)
        )
    }

    @Provides
    @Singleton
    fun provideTxnCategoryUseCases(repository: TxnCategoryRepository): TxnCategoryUseCase {
        return TxnCategoryUseCase(
            getTxnCategories = GetTxnCategories(repository),
            getTxnCategory = GetTxnCategory(repository),
            upsertTxnCategory = AddTxnCategory(repository),
            deleteTxnCategory = DeleteTxnCategory(repository)
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStore(@ApplicationContext appContext: Context): DataStore<AppSettings> {
        return DataStoreFactory.create(
            serializer = AppSettingsSerializer,
            produceFile = { appContext.dataStoreFile("app-settings.json") },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}