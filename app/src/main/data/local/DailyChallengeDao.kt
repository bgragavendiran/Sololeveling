"""import androidx.room.*

@Dao
interface DailyChallengeDao {
    @Query("SELECT * FROM dailychallenge")
    suspend fun getAllChallenges(): List<DailyChallenge>

    @Insert
    suspend fun insertAll(challenges: List<DailyChallenge>)

    @Update
    suspend fun updateChallenge(challenge: DailyChallenge)
}"""