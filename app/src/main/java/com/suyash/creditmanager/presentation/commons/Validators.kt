package com.suyash.creditmanager.presentation.commons

/**
 * Validates the length of the text input contained within this [TextInputState] object
 * against specified minimum and maximum limits.
 *
 * @receiver The [TextInputState] containing the text to validate.
 * @param minLength The minimum length allowed for the text input.
 * @param maxLength The maximum length allowed for the text input.
 * @return A [TextInputState] object with updated error state and message if the length
 *         validation fails, otherwise returns the original [TextInputState] unchanged.
 */
fun TextInputState<String>.validateMinMaxLength(
    minLength: Int,
    maxLength: Int
): TextInputState<String> {
    if (error) {
        return this
    }

    // Validate if the input data is within the specified length range
    if (data.length < minLength) {
        return copy(
            error = true,
            errorMessage = "Minimum allowed length is $minLength",
            displayError = true
        )
    }
    if (data.length > maxLength) {
        return copy(
            error = true,
            errorMessage = "Maximum allowed length is $maxLength",
            displayError = true
        )
    }

    return this
}

/**
 * Validates if the text input contained within this [TextInputState] object
 * represents a numeric value.
 *
 * @receiver The [TextInputState] containing the text to validate.
 * @return A [TextInputState] object with updated error state and message if the input
 *         is not numeric, otherwise returns the original [TextInputState] unchanged.
 */
fun TextInputState<String>.validateIsNumeric(): TextInputState<String> {
    if (error) {
        return this
    }

    // Validate if the input data can be converted to an integer
    if (data.toIntOrNull() == null) {
        return copy(error = true, errorMessage = "Must be a number", displayError = true)
    }

    return this
}

/**
 * Validates a credit card expiry date in MMYY format as the user types.
 *
 * @receiver The [TextInputState] containing the credit card expiry date input.
 * @return A [TextInputState] object with updated error state and message if the input
 *         is not a valid expiry date, otherwise returns the original [TextInputState] unchanged.
 */
fun TextInputState<String>.validateCCExpiry(): TextInputState<String> {
    if (error) {
        return this
    }

    // Validate input based on length and numeric ranges
    if (data.length != 4 ||
        !data.substring(0, 2).matches("^(0[1-9]|1[0-2])$".toRegex()) ||
        !data.substring(2).matches("^\\d{2}$".toRegex())
    ) {
        return copy(error = true, errorMessage = "Not a valid expiry date", displayError = true)
    }

    return this
}

/**
 * Validates numeric input within a specified range and optionally checks for a specific number of decimals.
 *
 * @param minValue The minimum allowed value for validation.
 * @param maxValue The maximum allowed value for validation.
 * @param numberOfDecimals Optional. The number of decimal places to consider (default is 0).
 * @return An updated [TextInputState] with error flags set if validation fails.
 */
fun TextInputState<String>.validateInRange(
    minValue: Int,
    maxValue: Int,
    numberOfDecimals: Int = 0
): TextInputState<String> {
    // Return early if there's already an error flagged
    if (error) {
        return this
    }

    // Check if the input is a valid integer
    if (data.toIntOrNull() == null) {
        return copy(error = true, errorMessage = "Not a valid number", displayError = true)
    }

    // Convert the input to an integer with consideration for decimal places
    val number: Int =
        if (numberOfDecimals > 0) (data.toInt() / 10 * numberOfDecimals) else data.toInt()

    // Validate if the number falls within the specified range
    if (number !in minValue..maxValue) {
        return copy(
            error = true,
            errorMessage = "Number should be in range $minValue and $maxValue",
            displayError = true
        )
    }

    return this
}