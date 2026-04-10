"""import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DailyChallengeModule { 
    @Provides
    @Singleton
    fun provideDailyChallengeRepository(): DailyChallengeRepository { 
        return DailyChallengeRepository(AppDatabase.createInstance().dailyChallengeDao()) 
    }

    @Provides
    @Singleton
    fun provideDailyChallengeRemoteDataSource(): DailyChallengeRemoteDataSource { 
        return MockDailyChallengeRemoteDataSource() // Replace with actual implementation later
    }
}"""