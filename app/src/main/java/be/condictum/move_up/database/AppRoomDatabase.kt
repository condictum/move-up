package be.condictum.move_up.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import be.condictum.move_up.database.dao.GoalsDao
import be.condictum.move_up.database.dao.LessonsDao
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.database.data.Lessons

@Database(entities = [Goals::class, Lessons::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun goalsDao(): GoalsDao
    abstract fun lessonsDao(): LessonsDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
