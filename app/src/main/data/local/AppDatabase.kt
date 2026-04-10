"""import androidx.room.*

@Database(version = 1, entities = [DailyChallenge::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun dailyChallengeDao(): DailyChallengeDao
}"""