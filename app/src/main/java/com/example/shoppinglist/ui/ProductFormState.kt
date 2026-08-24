package com.example.shoppinglist.ui

import com.example.shoppinglist.enums.IntervalUnit

interface ProductFormState {
    val name: String
    val quantity: String
    val intervalValue: String
    val intervalUnit: IntervalUnit
    val isFormValid: Boolean
    val showNameError: Boolean
    val showQuantityError: Boolean
    val showIntervalError: Boolean

    fun onNameChange(newValue: String)
    fun onQuantityChange(newValue: String)
    fun onIntervalValueChange(newValue: String)
    fun onIntervalUnitChange(newValue: IntervalUnit)
}
