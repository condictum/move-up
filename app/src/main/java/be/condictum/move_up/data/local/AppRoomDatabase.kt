package be.condictum.move_up.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import be.condictum.move_up.data.local.dao.GoalsDao
import be.condictum.move_up.data.local.dao.LessonsDao
import be.condictum.move_up.data.local.dao.ProfilesDao
import be.condictum.move_up.data.local.model.Goals
import be.condictum.move_up.data.local.model.Lessons
import be.condictum.move_up.data.local.model.Profiles

@Database(
    entities = [Profiles::class, Goals::class, Lessons::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun profilesDao(): ProfilesDao
    abstract fun goalsDao(): GoalsDao
    abstract fun lessonsDao(): LessonsDao
}
