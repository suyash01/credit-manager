package com.suyash.creditmanager.presentation.commons.visual_transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Visual transformation class for credit card expiry date input masking.
 * This class implements the [VisualTransformation] interface to modify the input text
 * for displaying and editing credit card expiry dates in MMYY format.
 */
class CCExpiryTransformation: VisualTransformation {

    /**
     * Transforms the input text to display and edit credit card expiry dates in MMYY format.
     *
     * @param text The annotated string representing the input text.
     * @return TransformedText containing the masked and translated text with offset mappings.
     */
    override fun filter(text: AnnotatedString): TransformedText {
        // Trim input text to ensure it's within the required length
        val originalText = text.text.trim()
        val trimmed = if (originalText.length > 4) originalText.substring(0..3) else text.text

        // Apply custom masking logic to format the expiry date in MM/YY format
        val out = expiryDateMask(trimmed)

        // Define offset translator for mapping cursor position between original and transformed text
        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                when (offset) {
                    in (2..4) -> offset + 1 // Adjust offsets for transformed text
                    else -> offset
                }

            override fun transformedToOriginal(offset: Int): Int =
                when (offset) {
                    in (3..5) -> offset - 1 // Adjust offsets for original text
                    else -> offset
                }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }

    /**
     * Masks the input credit card expiry date string to MM/YY format.
     *
     * @param expiryDate The input expiry date string to be formatted.
     * @return The formatted expiry date string in MM/YY format.
     */
    private fun expiryDateMask(expiryDate: String): String {
        var out = ""
        for (i in expiryDate.indices) {
            out += expiryDate[i]
            if (i == 1) out += "/" // Insert a slash '/' after the second character
        }
        return out
    }
}