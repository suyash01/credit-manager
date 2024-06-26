package com.suyash.creditmanager.domain.use_case.credit_card

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.repository.CreditCardRepository

class AddCreditCard(private val repository: CreditCardRepository) {

    suspend operator fun invoke(creditCard: CreditCard): Long {
        return repository.upsertCreditCard(creditCard)
    }
}