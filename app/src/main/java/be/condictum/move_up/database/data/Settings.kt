package be.condictum.move_up.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Settings")
class Settings(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "alarmType")
    val alarmType: String,
)