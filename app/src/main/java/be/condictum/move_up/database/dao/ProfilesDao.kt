package be.condictum.move_up.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import be.condictum.move_up.database.data.Profiles
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfilesDao {
    @Query("SELECT * from Profiles ORDER BY name ASC")
    fun getAllData(): Flow<List<Profiles>>

    @Query("SELECT * from Profiles WHERE id = :id")
    fun getData(id: Int): Flow<Profiles>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Profiles)

    @Update
    suspend fun update(data: Profiles)

    @Delete
    suspend fun delete(data: Profiles)
}