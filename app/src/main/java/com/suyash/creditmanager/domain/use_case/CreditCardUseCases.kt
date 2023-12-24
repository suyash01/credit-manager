package com.suyash.creditmanager.domain.use_case

import com.suyash.creditmanager.domain.use_case.credit_card.AddCreditCard
import com.suyash.creditmanager.domain.use_case.credit_card.DeleteCreditCard
import com.suyash.creditmanager.domain.use_case.credit_card.GetCreditCard
import com.suyash.creditmanager.domain.use_case.credit_card.GetCreditCards

data class CreditCardUseCases(
    val getCreditCards: GetCreditCards,
    val getCreditCard: GetCreditCard,
    val upsertCreditCard: AddCreditCard,
    val deleteCreditCard: DeleteCreditCard
)
