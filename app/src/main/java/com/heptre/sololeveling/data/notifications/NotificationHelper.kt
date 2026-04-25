package com.heptre.sololeveling.data.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.heptre.sololeveling.MainActivity

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_EXERCISE = "exercise_alert"
        const val CHANNEL_QUEST    = "quest_alert"

        const val NOTIF_EXERCISE_MORNING = 1001
        const val NOTIF_EXERCISE_EVENING = 1002
        const val NOTIF_STUDY_REMINDER   = 1003
        const val NOTIF_QUEST_GENERIC    = 1004
    }

    fun createChannels() {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val exerciseChannel = NotificationChannel(
            CHANNEL_EXERCISE,
            "Mandatory Exercise Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "High-priority alerts for mandatory exercise protocols"
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 1000)
        }

        val questChannel = NotificationChannel(
            CHANNEL_QUEST,
            "Quest Alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Quest reminders and daily resets"
        }

        nm.createNotificationChannels(listOf(exerciseChannel, questChannel))
    }

    fun showExerciseNotification(notifId: Int, timeLabel: String, questId: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("navigate_to", "exercise_countdown/$questId")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pi = PendingIntent.getActivity(
            context, notifId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_EXERCISE)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("SYSTEM ALERT — $timeLabel EXERCISE PROTOCOL")
            .setContentText("Mandatory exercise initiated. Report immediately, Hunter.")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(pi, true)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500, 200, 1000))
            .build()

        postNotification(notifId, notification)
    }

    fun showQuestNotification(title: String, body: String, notifId: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_QUEST)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        postNotification(notifId, notification)
    }

    private fun postNotification(id: Int, notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) return
        }
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(id, notification)
    }
}
