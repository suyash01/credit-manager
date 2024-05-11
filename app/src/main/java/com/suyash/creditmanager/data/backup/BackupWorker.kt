package com.suyash.creditmanager.data.backup

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.suyash.creditmanager.R
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.EMIUseCases
import com.suyash.creditmanager.domain.use_case.TransactionUseCase
import com.suyash.creditmanager.domain.use_case.TxnCategoryUseCase
import com.suyash.creditmanager.domain.util.LocalDateJSONConverter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.LocalDate

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val creditCardUseCases: CreditCardUseCases,
    private val emiUseCases: EMIUseCases,
    private val transactionUseCase: TransactionUseCase,
    private val txnCategoryUseCase: TxnCategoryUseCase
) : CoroutineWorker(appContext, workerParams) {

    private val notificationManager =
        applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private val gson = GsonBuilder().registerTypeAdapter(
        object : TypeToken<LocalDate>() {}.type,
        LocalDateJSONConverter()
    ).create()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val fileUri = inputData.getString(LOCATION_URI_KEY)?.toUri()
            ?: return@withContext Result.failure()

        val type = inputData.getString(TYPE_KEY) ?: return@withContext Result.failure()

        try {
            when (type) {
                "BACKUP" -> {
                    // Backup
                    setNotificationStart("Backup in progress")
                    val backupData = BackupData(
                        creditCards = creditCardUseCases.getCreditCards().first(),
                        emis = emiUseCases.getEMIs().first(),
                        transactions = transactionUseCase.getTransactions().first(),
                        txnCategories = txnCategoryUseCase.getTxnCategories().first()
                    )
                    writeBackupDataToJsonFile(fileUri, backupData)
                }

                "RESTORE" -> {
                    // Restore
                    setNotificationStart("Restore in progress")
                    val restoredBackupData = readBackupDataFromJsonFile(fileUri)
                    restoreData(restoredBackupData)
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        } finally {
            setNotificationEnd(
                "${type.lowercase().replaceFirstChar { it.uppercaseChar() }} Completed"
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun setNotificationStart(msg: String) {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(msg)
            .setAutoCancel(false)
            .setOngoing(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun setNotificationEnd(msg: String) {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(msg)
            .setAutoCancel(true)
            .setOngoing(false)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    data class BackupData(
        @SerializedName("creditCards")
        val creditCards: List<CreditCard>,
        @SerializedName("emis")
        val emis: List<EMI>,
        @SerializedName("transactions")
        val transactions: List<Transaction>,
        @SerializedName("txnCategories")
        val txnCategories: List<TxnCategory>
    )

    private fun writeBackupDataToJsonFile(uri: Uri, backupData: BackupData) {
        applicationContext.contentResolver.openOutputStream(uri)?.use { outputStream ->
            val jsonString = gson.toJson(backupData)
            val writer = OutputStreamWriter(outputStream)
            writer.write(jsonString)
            writer.flush()
        }
    }

    private fun readBackupDataFromJsonFile(uri: Uri): BackupData {
        try {
            applicationContext.contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = InputStreamReader(inputStream)
                return gson.fromJson(reader, BackupData::class.java)
            }
            return BackupData(
                creditCards = emptyList(),
                emis = emptyList(),
                transactions = emptyList(),
                txnCategories = emptyList()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return BackupData(
                creditCards = emptyList(),
                emis = emptyList(),
                transactions = emptyList(),
                txnCategories = emptyList()
            )
        }
    }

    private suspend fun restoreData(backupData: BackupData) {
        backupData.creditCards.forEach {
            it.id = 0
            creditCardUseCases.upsertCreditCard(it)
        }
        backupData.emis.forEach {
            it.id = 0
            emiUseCases.upsertEMI(it)
        }
        backupData.transactions.forEach {
            it.id = 0
            transactionUseCase.upsertTransaction(it)
        }
        backupData.txnCategories.forEach {
            it.id = 0
            txnCategoryUseCase.upsertTxnCategory(it)
        }
    }

    companion object {
        private const val CHANNEL_ID = "BackupChannel"
        private const val NOTIFICATION_ID = 1
        private const val TAG_AUTO = "BackupWorker"
        private const val TAG_MANUAL = "$TAG_AUTO:manual"

        private const val LOCATION_URI_KEY = "location_uri"
        private const val TYPE_KEY = "type"

        fun isManualJobRunning(context: Context): Boolean {
            return WorkManager.getInstance(context).isRunning(TAG_MANUAL)
        }

        fun startNow(context: Context, uri: Uri, type: String) {
            val inputData = workDataOf(
                LOCATION_URI_KEY to uri.toString(),
                TYPE_KEY to type
            )
            val request = OneTimeWorkRequestBuilder<BackupWorker>()
                .addTag(TAG_MANUAL)
                .setInputData(inputData)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(TAG_MANUAL, ExistingWorkPolicy.KEEP, request)
        }

        private fun WorkManager.isRunning(tag: String): Boolean {
            val list = this.getWorkInfosByTag(tag).get()
            return list.any { it.state == WorkInfo.State.RUNNING }
        }
    }
}
