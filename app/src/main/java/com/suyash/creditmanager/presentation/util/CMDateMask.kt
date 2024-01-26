package com.suyash.creditmanager.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CMDateMask: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length > 4) text.text.substring(0..3) else text.text
        val out = CMUtils.expiryDateMask(trimmed)

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                when (offset) {
                    in (2..4) -> offset + 1
                    else -> offset
                }

            override fun transformedToOriginal(offset: Int): Int =
                when (offset) {
                    in (3..5) -> offset - 1
                    else -> offset
                }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}