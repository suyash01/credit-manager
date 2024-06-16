package com.suyash.creditmanager.presentation.transactions.component

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
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.util.TransactionType
import com.suyash.creditmanager.presentation.commons.CMUtils
import com.suyash.creditmanager.ui.theme.CreditForeground
import com.suyash.creditmanager.ui.theme.DebitForeground

@Composable
fun TransactionItem(
    transaction: Transaction,
    creditCard: CreditCard?,
    countryCode: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = CMUtils.currencyMask(transaction.amount, countryCode),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if(transaction.type == TransactionType.DEBIT) DebitForeground else CreditForeground
                    )
                    Text(
                        text = transaction.category?:"",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = creditCard?.cardName?:"Deleted Credit Card",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = creditCard?.last4Digits?:"XXXX",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}