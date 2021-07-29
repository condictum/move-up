package be.condictum.move_up.database.dao

import androidx.room.*
import be.condictum.move_up.database.data.Lessons
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonsDao {
    @Query("SELECT * from Lessons ORDER BY name ASC")
    fun getAllData(): Flow<List<Lessons>>

    @Query("SELECT * from Lessons WHERE id = :id")
    fun getLesson(id: Int): Flow<Lessons>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Lessons)

    @Update
    suspend fun update(data: Lessons)

    @Delete
    suspend fun delete(data: Lessons)
}