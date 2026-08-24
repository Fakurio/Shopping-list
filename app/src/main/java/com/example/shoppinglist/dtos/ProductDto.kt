package com.example.shoppinglist.dtos

import android.os.Parcelable
import com.example.shoppinglist.enums.IntervalUnit
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
data class ProductDto(
    val id: Int,
    val name: String,
    val quantity: Long,
    val lastBoughtDate: Date?,
    val intervalValue: Int,
    val intervalUnit: IntervalUnit,
    val activeNotificationId: UUID?,
    val isTracked: Boolean
) : Parcelable

val ProductDto.formattedInterval: String
    get() {
        val unitName = intervalUnit.name.lowercase()
        val pluralUnit = if (intervalValue == 1) {
            unitName.removeSuffix("s")
        } else {
            unitName
        }
        return "$intervalValue $pluralUnit"
    }
