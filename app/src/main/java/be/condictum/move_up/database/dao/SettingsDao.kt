package be.condictum.move_up.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import be.condictum.move_up.database.data.Settings
import androidx.room.Dao as Dao

@Dao
interface SettingsDao {
    @Query("SELECT * from Settings ORDER BY alarmSound ASC")
    fun getAllData(): LiveData<List<Settings>>

    @Query("SELECT * from Settings WHERE id = :id")
    fun getData(id: Int): Settings

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Settings)

    @Update
    suspend fun update(data: Settings)

    @Delete
    suspend fun delete(data: Settings)
}