package com.suyash.creditmanager.presentation.emi_detail

sealed class EMIDetailEvent {
    data object BackPressed: EMIDetailEvent()
}