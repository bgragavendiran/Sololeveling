"""
import androidx.annotation.NonNull

data class DailyChallenge(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val dueDate: Long, // Timestamp in milliseconds
    var isCompleted: Boolean = false
) {
    companion object {
        const val NO_DUE_DATE = -1L
    }
}
"""