package be.condictum.move_up.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "Goals")
data class Goals(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val dataName: String,
    @ColumnInfo(name = "date")
    val dataDate: Date,
)