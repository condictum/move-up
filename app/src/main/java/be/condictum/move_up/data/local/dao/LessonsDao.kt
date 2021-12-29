package be.condictum.move_up.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import be.condictum.move_up.data.local.model.Lessons

@Dao
interface LessonsDao {
    @Query("SELECT * from Lessons WHERE goals_id = :goalsId ORDER BY name ASC")
    fun getAllDataByGoalsId(goalsId: Int): LiveData<List<Lessons>>

    @Query("SELECT * from Lessons WHERE id = :id")
    fun getLesson(id: Int): LiveData<Lessons>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Lessons)

    @Query("DELETE from Lessons WHERE goals_id = :goalsId")
    suspend fun deleteDataByGoalsId(goalsId: Int)

    @Update
    suspend fun update(data: Lessons)

    @Delete
    suspend fun delete(data: Lessons)
}