package com.example.shoppinglist.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shoppinglist.enums.IntervalUnit
import java.util.concurrent.TimeUnit

class NotificationHelper(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleNotification(productId: Int, productName: String, intervalValue: Int, intervalUnit: IntervalUnit) {
        val (duration, timeUnit) = when (intervalUnit) {
            IntervalUnit.MINUTES -> intervalValue.toLong() to TimeUnit.MINUTES
            IntervalUnit.DAYS -> intervalValue.toLong() * 24 to TimeUnit.HOURS
            IntervalUnit.WEEKS -> intervalValue.toLong() * 7 * 24 to TimeUnit.HOURS
            IntervalUnit.MONTHS -> intervalValue.toLong() * 30 * 24 to TimeUnit.HOURS
        }

        val inputData = Data.Builder()
            .putInt(ProductNotificationWorker.KEY_PRODUCT_ID, productId)
            .putString(ProductNotificationWorker.KEY_PRODUCT_NAME, productName)
            .putInt(ProductNotificationWorker.KEY_INTERVAL_VALUE, intervalValue)
            .putString(ProductNotificationWorker.KEY_INTERVAL_UNIT, intervalUnit.name)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ProductNotificationWorker>()
            .setInitialDelay(duration, timeUnit)
            .setInputData(inputData)
            .addTag("product_$productId")
            .build()

        workManager.enqueueUniqueWork(
            "product_$productId",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelNotification(productId: Int) {
        workManager.cancelUniqueWork("product_$productId")
        workManager.pruneWork()
    }
}
