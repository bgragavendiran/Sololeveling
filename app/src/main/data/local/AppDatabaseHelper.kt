"""import android.content.Context
import androidx.room.Room

object AppDatabase { 
    fun createInstance(context: Context): AppDatabase { 
        return Room.databaseBuilder(context, AppDatabase::class.java, "daily-challenge-db")
            .build()
    }
}"""