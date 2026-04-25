package com.heptre.sololeveling.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.heptre.sololeveling.data.db.SoloLevelingDatabase
import com.heptre.sololeveling.data.notifications.NotificationHelper
import com.heptre.sololeveling.data.tts.TtsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notif = NotificationHelper(context)
        val tts   = TtsManager(context)

        when (intent.action) {
            QuestAlarmScheduler.ACTION_EXERCISE_MORNING -> {
                notif.showExerciseNotification(NotificationHelper.NOTIF_EXERCISE_MORNING, "MORNING", questId = 101)
                tts.speak("Hunter. Morning exercise protocol has been activated. You have until noon. Report immediately.")
                QuestAlarmScheduler(context).scheduleMorningExercise() // reschedule for next day
            }

            QuestAlarmScheduler.ACTION_EXERCISE_EVENING -> {
                notif.showExerciseNotification(NotificationHelper.NOTIF_EXERCISE_EVENING, "EVENING", questId = 102)
                tts.speak("Hunter. Evening exercise protocol activated. Complete your training before midnight.")
                QuestAlarmScheduler(context).scheduleEveningExercise()
            }

            QuestAlarmScheduler.ACTION_DAILY_RESET -> {
                val pending = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db  = SoloLevelingDatabase.getDatabase(context, this)
                        val now = System.currentTimeMillis()
                        db.questDao().resetDailySystemQuests(now)
                        db.questDao().resetBiweeklySystemQuests(now)
                    } finally {
                        pending.finish()
                    }
                }
                notif.showQuestNotification("SYSTEM RESET", "New day. New quests. Rise, Hunter.", NotificationHelper.NOTIF_QUEST_GENERIC)
                tts.speak("New cycle initiated. Daily quests have been reset. Prove your worth, Hunter.")
                QuestAlarmScheduler(context).scheduleMidnightReset()
            }

            QuestAlarmScheduler.ACTION_STUDY_REMINDER -> {
                val pending = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db   = SoloLevelingDatabase.getDatabase(context, this)
                        val quest = db.questDao().getQuestById(103)
                        if (quest != null && !quest.isCompleted) {
                            val remaining = quest.targetValue - quest.currentValue
                            notif.showQuestNotification(
                                "NEURAL ENHANCEMENT ALERT",
                                "$remaining minutes of study remaining today. Do not fail, Hunter.",
                                NotificationHelper.NOTIF_STUDY_REMINDER
                            )
                            tts.speak("Hunter. You still need $remaining minutes of study today. The System is watching.")
                        }
                    } finally {
                        pending.finish()
                    }
                }
                QuestAlarmScheduler(context).scheduleStudyReminder()
            }

            Intent.ACTION_BOOT_COMPLETED -> {
                // Reschedule all alarms after device reboot
                QuestAlarmScheduler(context).scheduleAll()
            }
        }
    }
}
