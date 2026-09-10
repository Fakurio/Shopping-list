package com.example.shoppinglist.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.shoppinglist.MainActivity
import androidx.core.net.toUri
import com.example.shoppinglist.enums.IntervalUnit

class ProductNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val productName = inputData.getString(KEY_PRODUCT_NAME) ?: return Result.failure()
        val productId = inputData.getInt(KEY_PRODUCT_ID, -1)
        val intervalValue = inputData.getInt(KEY_INTERVAL_VALUE, -1)
        val intervalUnitStr = inputData.getString(KEY_INTERVAL_UNIT)

        if (productId != -1) {
            showNotification(productName, productId)

            // Reschedule the next one-time work
            if (intervalValue > 0 && intervalUnitStr != null) {
                try {
                    val intervalUnit = IntervalUnit.valueOf(intervalUnitStr)
                    val notificationHelper = NotificationHelper(applicationContext)
                    notificationHelper.scheduleNotification(productId, productName, intervalValue, intervalUnit)
                } catch (e: Exception) {
                    // Ignore if enum parsing fails
                }
            }
        }

        return Result.success()
    }

    private fun showNotification(productName: String, productId: Int) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "product_restock_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Product Restock Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(
            Intent.ACTION_VIEW,
            "shoppinglist://restock/$productId".toUri(),
            applicationContext,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            productId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Restock Needed!")
            .setContentText("Time to buy more $productName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Using productId as the notification ID ensures that if a new notification
            // for the same product fires, it overwrites the existing one in the drawer.
            notificationManager.notify(productId, notification)
        }
    }

    companion object {
        const val KEY_PRODUCT_NAME = "product_name"
        const val KEY_PRODUCT_ID = "product_id"
        const val KEY_INTERVAL_VALUE = "interval_value"
        const val KEY_INTERVAL_UNIT = "interval_unit"
    }
}
