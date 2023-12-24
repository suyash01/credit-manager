package com.suyash.creditmanager.domain.use_case.credit_card

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.InvalidCreditCardException
import com.suyash.creditmanager.domain.repository.CreditCardRepository
import kotlin.jvm.Throws

class AddCreditCard(private val repository: CreditCardRepository) {

    @Throws(InvalidCreditCardException::class)
    suspend operator fun invoke(creditCard: CreditCard) {
        if(creditCard.cardName.isBlank() || creditCard.last4Digits.isBlank() || creditCard.expiryDate.isBlank()) {
            throw InvalidCreditCardException("Fields cannot be blank")
        }
        repository.upsertCreditCard(creditCard)
    }
}