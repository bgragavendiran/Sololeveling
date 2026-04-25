package com.heptre.sololeveling.data

enum class QuestType {
    STEPS,      // Auto-tracked via Health Connect (10,000 steps/day)
    EXERCISE,   // Rep countdown with mandatory alarm (25x4 every 12h)
    STUDY,      // User logs time (Duolingo, books, courses — target 2h/day)
    GYM,        // User logs weekly sessions (3x1h per week)
    CUSTOM      // User-created with optional frequency and deadline
}

enum class QuestFrequency {
    DAILY,      // Resets at midnight
    BIWEEKLY,   // Every 12 hours (morning + evening exercise)
    WEEKLY_3X,  // Three times per week (gym)
    ONCE        // One-time quest with optional deadline
}
