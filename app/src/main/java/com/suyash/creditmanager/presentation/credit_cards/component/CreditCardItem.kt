package com.suyash.creditmanager.presentation.credit_cards.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.util.CardType
import com.suyash.creditmanager.presentation.util.CCUtils

@Composable
fun CreditCardItem(
    creditCard: CreditCard,
    countryCode: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.size(45.dp),
                painter = painterResource(id = creditCard.cardType.id),
                contentDescription = "Card Type"
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = creditCard.cardName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = CCUtils.currencyMask(creditCard.limit, countryCode),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (creditCard.cardType == CardType.AMEX) {
                            "XXXX-XXXXXX-${creditCard.last4Digits}"
                        } else {
                            "XXXX-XXXX-XXXX-${creditCard.last4Digits}"
                        },
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = creditCard.expiryDate.substring(0..1) +
                                "/" +
                                creditCard.expiryDate.substring(2),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}