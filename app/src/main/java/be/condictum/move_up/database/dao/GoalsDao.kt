package be.condictum.move_up.database.dao

import androidx.room.*
import be.condictum.move_up.database.data.Goals
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalsDao {
    @Query("SELECT * from Goals ORDER BY name ASC")
    fun getAllData(): Flow<List<Goals>>

    @Query("SELECT * from Goals WHERE id = :id")
    fun getData(id: Int): Flow<Goals>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Goals)

    @Update
    suspend fun update(data: Goals)

    @Delete
    suspend fun delete(data: Goals)
}
