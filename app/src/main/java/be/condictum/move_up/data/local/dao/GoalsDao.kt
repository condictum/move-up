package be.condictum.move_up.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import be.condictum.move_up.data.local.model.Goals

@Dao
interface GoalsDao {
    @Query("SELECT * from Goals WHERE profiles_id = :profileId ORDER BY name ASC")
    fun getAllLiveDataByProfileId(profileId: Int): LiveData<List<Goals>>

    @Query("SELECT * from Goals ORDER BY date ASC")
    fun getAllGoals(): LiveData<List<Goals>>

    @Query("SELECT * from Goals WHERE profiles_id = :profileId ORDER BY name ASC")
    fun getAllDataByProfileId(profileId: Int): LiveData<List<Goals>>

    @Query("SELECT * from Goals WHERE id = :id")
    fun getData(id: Int): LiveData<Goals>

    @Query("DELETE from Goals WHERE profiles_id = :profileId")
    suspend fun deleteDataByProfileId(profileId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Goals)

    @Update
    suspend fun update(data: Goals)

    @Delete
    suspend fun delete(data: Goals)
}
