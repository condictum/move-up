package be.condictum.move_up.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Lessons",
    foreignKeys = [ForeignKey(
        entity = Goals::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("goals_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Lessons(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val lessonName: String,
    @ColumnInfo(name = "score")
    val lessonScore: Double,
    @ColumnInfo(name = "goals_id")
    val goalsId: Int,
)