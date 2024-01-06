package com.suyash.creditmanager.presentation.transactions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.util.TransactionType
import com.suyash.creditmanager.presentation.util.CCUtils
import com.suyash.creditmanager.ui.theme.Credit
import com.suyash.creditmanager.ui.theme.Debit
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(
    transaction: Transaction,
    countryCode: String,
    dateFormat: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color = if(transaction.type == TransactionType.DEBIT) Debit else Credit)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = CCUtils.currencyMask(transaction.amount, countryCode),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = transaction.type.name,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = transaction.date.format(DateTimeFormatter.ofPattern(dateFormat)),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}