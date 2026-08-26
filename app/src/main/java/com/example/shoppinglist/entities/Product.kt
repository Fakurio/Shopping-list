package com.example.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoppinglist.enums.IntervalUnit
import java.util.Date
import java.util.UUID

@Entity(tableName = "products")
data class Product (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Long,
    @ColumnInfo(name = "last_bought_date") val lastBoughtDate: Date?,
    @ColumnInfo(name = "interval_value") val intervalValue: Int?,
    @ColumnInfo(name = "interval_unit") val intervalUnit: IntervalUnit?,
    @ColumnInfo(name = "active_notification_id") val activeNotificationId: UUID?,
    @ColumnInfo(name = "is_tracked") val isTracked: Boolean
)