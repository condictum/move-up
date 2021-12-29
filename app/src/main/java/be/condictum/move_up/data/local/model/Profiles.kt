package be.condictum.move_up.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Profiles")
data class Profiles(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "surname")
    val surname: String,
    @ColumnInfo(name = "age")
    val age: Int
)