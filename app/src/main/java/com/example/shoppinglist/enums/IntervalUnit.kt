package com.example.shoppinglist.enums

import kotlinx.serialization.Serializable

@Serializable
enum class IntervalUnit {
    MINUTES, // TEMPORARY for testing
    DAYS,
    WEEKS,
    MONTHS
}
