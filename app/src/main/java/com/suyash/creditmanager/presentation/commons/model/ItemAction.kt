package com.suyash.creditmanager.presentation.commons.model

import androidx.compose.ui.graphics.vector.ImageVector

data class ItemAction(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit,
    val iconName: String? = null
)
