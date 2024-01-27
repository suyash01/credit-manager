package com.suyash.creditmanager.presentation.emi_details

sealed class EMIDetailEvent {
    data object BackPressed: EMIDetailEvent()
}