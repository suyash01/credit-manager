package com.suyash.creditmanager.presentation.commons.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suyash.creditmanager.domain.util.order.Order
import com.suyash.creditmanager.domain.util.order.OrderType
import kotlin.reflect.full.primaryConstructor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Order> CustomSortingBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    orderList: List<T>,
    currentOrder: T,
    sort: (T) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = modifier.padding(bottom = 16.dp)
        ) {
            orderList.forEach {
                Row(
                    modifier = Modifier
                        .clickable {
                            onDismissRequest()
                            if (currentOrder::class == it::class) {
                                sort(
                                    currentOrder::class.primaryConstructor!!.call(
                                        currentOrder.orderType.getReverse(),
                                        currentOrder.label
                                    )
                                )
                            } else {
                                sort(it)
                            }
                        }
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (currentOrder::class == it::class) {
                        if (currentOrder.orderType == OrderType.Ascending) {
                            Icon(Icons.Filled.ArrowUpward, "Ascending")
                        } else if (currentOrder.orderType == OrderType.Descending) {
                            Icon(Icons.Filled.ArrowDownward, "Descending")
                        }
                    }
                    Text(text = it.label, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}