package com.suyash.creditmanager.presentation.commons.visual_transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.suyash.creditmanager.presentation.commons.formatCurrencyAmount

class CurrencyTransformation(
    private val countryCode: String
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val trimText = text.text.trim()
        val originalText = if(trimText.length > 8) trimText.substring(0..7) else trimText
        if (originalText.isEmpty() || originalText.toFloatOrNull() == null) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formattedText = formatCurrencyAmount(originalText.toFloat(), 0, countryCode, false)

        val currencyOffsetTranslator = object : OffsetMapping {
            private val originalLength: Int = originalText.length
            private val indexes = findDigitIndexes(originalText, formattedText)

            private fun findDigitIndexes(firstString: String, secondString: String): List<Int> {
                val digitIndexes = mutableListOf<Int>()
                var currentIndex = 0
                for (digit in firstString) {
                    val index = secondString.indexOf(digit, currentIndex)
                    if (index != -1) {
                        digitIndexes.add(index)
                        currentIndex = index + 1
                    } else {
                        return emptyList()
                    }
                }
                return digitIndexes
            }

            override fun originalToTransformed(offset: Int): Int {
                if (offset >= originalLength) {
                    return indexes.last() + 1
                }
                return indexes[offset]
            }

            override fun transformedToOriginal(offset: Int): Int {
                return indexes.indexOfFirst { it >= offset }.takeIf { it != -1 } ?: originalLength
            }
        }

        return TransformedText(AnnotatedString(formattedText), currencyOffsetTranslator)
    }

}