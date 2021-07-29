package be.condictum.move_up.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val dataName: String,
    @ColumnInfo(name = "price")
    val dataPrice: Double,
    @ColumnInfo(name = "quantity")
    val dataCount: Int,
)