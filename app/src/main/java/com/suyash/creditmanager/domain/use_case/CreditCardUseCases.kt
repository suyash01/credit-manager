package com.suyash.creditmanager.domain.use_case

data class CreditCardUseCases(
    val getCreditCards: GetCreditCards,
    val getCreditCard: GetCreditCard,
    val upsertCreditCard: AddCreditCard,
    val deleteCreditCard: DeleteCreditCard
)
