package com.suyash.creditmanager.presentation.emis.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.util.DateFormat
import com.suyash.creditmanager.presentation.commons.CMUtils
import java.time.LocalDate

@Composable
fun EMIItem(
    emi: EMI,
    countryCode: String,
    dateFormat: DateFormat,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
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
                        text = emi.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = CMUtils.currencyMask(emi.amount, countryCode),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = CMUtils.formatDate(emi.date, dateFormat),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = "${emi.emisPaid()}/${emi.months}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = CMUtils.formatDate(emi.date.plusMonths(emi.months.toLong() - 1), dateFormat),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun EMIItemPreview() {
    EMIItem(
        emi = EMI("Preview EMI", 10000F, 15.99F, 9, null, LocalDate.now().minusMonths(3), 18F),
        countryCode = "IN",
        dateFormat = DateFormat.DDMMYYYY,
        modifier = Modifier.fillMaxWidth()
    )
}