package com.suyash.creditmanager.domain.use_case.credit_card

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.repository.CreditCardRepository

class GetCreditCard(
    private val repository: CreditCardRepository
) {
    suspend operator fun invoke(id: Int): CreditCard? {
        return repository.getCreditCard(id)
    }
}