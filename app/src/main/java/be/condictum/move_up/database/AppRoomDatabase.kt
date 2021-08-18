package be.condictum.move_up.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import be.condictum.move_up.database.dao.GoalsDao
import be.condictum.move_up.database.dao.LessonsDao
import be.condictum.move_up.database.dao.ProfilesDao
import be.condictum.move_up.database.dao.SettingsDao
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.database.data.Lessons
import be.condictum.move_up.database.data.Profiles
import be.condictum.move_up.database.data.Settings

@Database(
    entities = [Profiles::class, Goals::class, Lessons::class,Settings::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun profilesDao(): ProfilesDao
    abstract fun goalsDao(): GoalsDao
    abstract fun lessonsDao(): LessonsDao
    abstract fun settingsDao():SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "move_up_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
