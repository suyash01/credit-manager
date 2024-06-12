package com.suyash.creditmanager.presentation.commons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
    ) {
    data object CreditCardsScreen: Screen(
        "cc_screen",
        "Cards",
        Icons.Filled.CreditCard,
        Icons.Outlined.CreditCard
    )
    data object TransactionsScreen: Screen(
        "txns_screen",
        "Transactions",
        Icons.Filled.Receipt,
        Icons.Outlined.Receipt
    )
    data object EMIsScreen: Screen(
        "emis_screen",
        "EMIs",
        Icons.Filled.Payments,
        Icons.Outlined.Payments
    )
    data object SettingsScreen: Screen(
        "settings_screen",
        "Settings",
        Icons.Filled.Settings,
        Icons.Outlined.Settings
    )
    data object AddEditCCScreen: Screen(
        "add_edit_cc_screen",
        "Add/Edit Card",
        Icons.Filled.Add,
        Icons.Outlined.Add
    )

    data object AddEditTxnScreen: Screen(
        "add_edit_txn_screen",
        "Add/Edit Transaction",
        Icons.Filled.Add,
        Icons.Outlined.Add
    )
    data object AddEditEMIScreen: Screen(
        "add_edit_emi_screen",
        "Add/Edit EMI",
        Icons.Filled.Add,
        Icons.Outlined.Add
    )

    data object EMIDetailScreen: Screen(
        "emi_detail",
        "EMI Detail",
        Icons.Filled.Info,
        Icons.Outlined.Info
    )

    data object TxnCategoriesScreen: Screen(
        "txn_categories",
        "Transaction Categories",
        Icons.Filled.Category,
        Icons.Outlined.Category
    )

    companion object {
        val bottomBarScreens: List<Screen> = listOf(
            CreditCardsScreen,
            TransactionsScreen,
            EMIsScreen,
            SettingsScreen
        )
    }
}