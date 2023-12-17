package com.suyash.creditmanager.domain.use_case

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.repository.CreditCardRepository

class DeleteCreditCard(
    private val repository: CreditCardRepository
) {
    suspend operator fun invoke(creditCard: CreditCard) {
        return repository.deleteCreditCard(creditCard)
    }
}