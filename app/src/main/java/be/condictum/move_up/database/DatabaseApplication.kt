package be.condictum.move_up.database

import android.app.Application

class DatabaseApplication : Application() {
    val database: AppRoomDatabase by lazy { AppRoomDatabase.getDatabase(this) }
}