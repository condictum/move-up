package be.condictum.move_up.di

import android.content.Context
import androidx.room.Room
import be.condictum.move_up.data.local.AppRoomDatabase
import be.condictum.move_up.data.local.dao.GoalsDao
import be.condictum.move_up.data.local.dao.LessonsDao
import be.condictum.move_up.data.local.dao.ProfilesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppRoomDatabase::class.java,
            "move_up_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGoalsDao(database: AppRoomDatabase): GoalsDao {
        return database.goalsDao()
    }

    @Provides
    @Singleton
    fun provideLessonsDao(database: AppRoomDatabase): LessonsDao {
        return database.lessonsDao()
    }

    @Provides
    @Singleton
    fun provideProfilesDao(database: AppRoomDatabase): ProfilesDao {
        return database.profilesDao()
    }
}