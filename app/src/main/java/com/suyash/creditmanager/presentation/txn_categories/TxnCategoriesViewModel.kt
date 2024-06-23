package com.suyash.creditmanager.presentation.txn_categories

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.use_case.TxnCategoryUseCases
import com.suyash.creditmanager.domain.util.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TxnCategoriesViewModel @Inject constructor(
    private val txnCategoryUseCase: TxnCategoryUseCases
): ViewModel() {

    private val _txnCategories = mutableStateOf(emptyList<TxnCategory>())
    val txnCategories: State<List<TxnCategory>> = _txnCategories

    private val _type = mutableStateOf(TransactionType.DEBIT)
    val type: State<TransactionType> = _type

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _selectedId = mutableIntStateOf(0)
    val selectedId: State<Int> = _selectedId

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getTxnCategoriesJob: Job? = null

    init {
        getTxnCategories()
    }

    fun onEvent(event: TxnCategoriesEvent) {
        when(event) {
            is TxnCategoriesEvent.BackPressed -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
            is TxnCategoriesEvent.CategorySelected -> {
                _type.value = event.txnCategory.type
                _name.value = event.txnCategory.name
                _selectedId.intValue = event.txnCategory.id
            }
            is TxnCategoriesEvent.TypeSelected -> {
                _type.value = event.type
            }
            is TxnCategoriesEvent.NameEntered -> {
                _name.value = event.name
            }
            is TxnCategoriesEvent.UpsertCategory -> {
                viewModelScope.launch {
                    if(name.value.isEmpty()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Name cannot be empty"))
                        return@launch
                    }
                    txnCategoryUseCase.upsertTxnCategory(
                        TxnCategory(
                            type = type.value,
                            name = name.value,
                            id = selectedId.value
                        )
                    )
                }
            }
            is TxnCategoriesEvent.DeleteCategory -> {
                viewModelScope.launch {
                    txnCategoryUseCase.deleteTxnCategory(
                        TxnCategory(
                            type = type.value,
                            name = name.value,
                            id = selectedId.value
                        )
                    )
                }
            }
        }
    }

    private fun getTxnCategories() {
        getTxnCategoriesJob?.cancel()
        getTxnCategoriesJob = txnCategoryUseCase.getTxnCategories().onEach {
            _txnCategories.value = it
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data object NavigateUp: UiEvent()
    }
}