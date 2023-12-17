package com.suyash.creditmanager.presentation.credit_cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.presentation.credit_cards.component.CreditCardItem
import com.suyash.creditmanager.presentation.util.Screen

@Composable
fun CreditCardsScreen(
    navController: NavController,
    viewModel: CreditCardsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditCCScreen.route) }
            ) {
                Icon(Icons.Filled.Add, "Add Credit Card")
            }
        }
    ) {
        contentPadding ->
        LazyColumn (
            modifier = Modifier.padding(contentPadding)
        ) {
            items(state.creditCards) { creditCard ->
                CreditCardItem(
                    creditCard = creditCard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                Screen.AddEditCCScreen.route + "?ccId=${creditCard.id}"
                            )
                        }
                )
            }
        }
    }
}