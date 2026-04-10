"""import javax.inject.Singleton

@Singleton
class DailyChallengeRepository(private val dailyChallengeDao: DailyChallengeDao) {
    suspend fun getAllChallenges(): List<DailyChallenge> { return dailyChallengeDao.getAllChallenges() }

    suspend fun insertAllChallenges(challenges: List<DailyChallenge>) { dailyChallengeDao.insertAll(challenges)}

    suspend fun updateChallenge(challenge: DailyChallenge) { dailyChallengeDao.updateChallenge(challenge)}
}"""