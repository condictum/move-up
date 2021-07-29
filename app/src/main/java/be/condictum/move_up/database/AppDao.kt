package be.condictum.move_up.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * from AppData ORDER BY name ASC")
    fun getAll(): Flow<List<AppData>>

    @Query("SELECT * from AppData WHERE id = :id")
    fun get(id: Int): Flow<AppData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: AppData)

    @Update
    suspend fun update(data: AppData)

    @Delete
    suspend fun delete(data: AppData)
}
