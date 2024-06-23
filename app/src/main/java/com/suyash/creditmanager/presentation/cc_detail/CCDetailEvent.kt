package com.suyash.creditmanager.presentation.cc_detail

sealed class CCDetailEvent {
    data object BackPressed: CCDetailEvent()
}