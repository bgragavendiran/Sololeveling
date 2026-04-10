"""import androidx.lifecycle.*
import kotlinx.coroutines.*

class DailyChallengeViewModel(private val dailyChallengeRepository: DailyChallengeRepository) : ViewModel() { 
    private val _dailyChallenges = MutableLiveData<List<DailyChallenge>>()
    val dailyChallenges: LiveData<List<DailyChallenge>> get() = _dailyChallenges

    fun loadChallenges() { 
        viewModelScope.launch(Dispatchers.IO) { 
            _dailyChallenges.postValue(dailyChallengeRepository.getAllChallenges()) 
        } 
    }
}"""