"""import java.util.* 

class MockDailyChallengeRemoteDataSource : DailyChallengeRemoteDataSource { 
    override suspend fun getDailyChallenges(): List<DailyChallenge> { 
        return listOf(
            DailyChallenge(1, "Run 5km", "Go for a run", Calendar.getInstance().timeInMillis), 
            DailyChallenge(2, "Read 30 pages", "Read a book", Calendar.getInstance().timeInMillis)
        ) 
    }
}"""