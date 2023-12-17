package com.suyash.creditmanager.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CCDateMask: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length > 4) text.text.substring(0..3) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i==1) out += "/"
        }

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