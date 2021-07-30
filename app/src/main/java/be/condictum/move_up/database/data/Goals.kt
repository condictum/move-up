package be.condictum.move_up.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "Goals",
    foreignKeys = [ForeignKey(
        entity = Profiles::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("profiles_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Goals(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val dataName: String,
    @ColumnInfo(name = "date")
    val dataDate: Date,
    @ColumnInfo(name = "profiles_id")
    val profilesId: Int,
)