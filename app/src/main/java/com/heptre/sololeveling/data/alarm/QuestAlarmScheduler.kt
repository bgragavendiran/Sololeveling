package com.heptre.sololeveling.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import java.util.Calendar

class QuestAlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val ACTION_EXERCISE_MORNING = "com.heptre.sololeveling.EXERCISE_MORNING"
        const val ACTION_EXERCISE_EVENING = "com.heptre.sololeveling.EXERCISE_EVENING"
        const val ACTION_DAILY_RESET      = "com.heptre.sololeveling.DAILY_RESET"
        const val ACTION_STUDY_REMINDER   = "com.heptre.sololeveling.STUDY_REMINDER"
    }

    /** Schedule all recurring system alarms. Call once on first launch and after BOOT_COMPLETED. */
    fun scheduleAll() {
        scheduleMorningExercise()
        scheduleEveningExercise()
        scheduleMidnightReset()
        scheduleStudyReminder()
    }

    fun scheduleMorningExercise() {
        schedule(ACTION_EXERCISE_MORNING, requestCode = 101, hour = 6, minute = 0)
    }

    fun scheduleEveningExercise() {
        schedule(ACTION_EXERCISE_EVENING, requestCode = 102, hour = 18, minute = 0)
    }

    fun scheduleMidnightReset() {
        schedule(ACTION_DAILY_RESET, requestCode = 200, hour = 0, minute = 0)
    }

    fun scheduleStudyReminder() {
        // Remind at 20:00 if study target is not yet met
        schedule(ACTION_STUDY_REMINDER, requestCode = 103, hour = 20, minute = 0)
    }

    private fun schedule(action: String, requestCode: Int, hour: Int, minute: Int) {
        val triggerMs = nextAlarmTime(hour, minute)
        val intent = Intent(context, QuestAlarmReceiver::class.java).apply { this.action = action }
        val pi = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.SCHEDULE_EXACT_ALARM
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMs, pi)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMs, pi)
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMs, pi)
        }
    }

    private fun nextAlarmTime(hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }
        return cal.timeInMillis
    }
}
