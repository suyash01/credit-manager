package com.suyash.creditmanager.presentation.util

sealed class Screen(val route: String) {
    object CreditCardsScreen: Screen("cc_screen")
    object AddEditCCScreen: Screen("add_edit_cc_screen")
}