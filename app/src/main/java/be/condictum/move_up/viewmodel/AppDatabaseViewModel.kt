package be.condictum.move_up.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.condictum.move_up.database.dao.GoalsDao
import be.condictum.move_up.database.dao.LessonsDao
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.database.data.Lessons
import kotlinx.coroutines.launch
import java.util.*

class AppDatabaseViewModel(private val goalsDao: GoalsDao, private val lessonsDao: LessonsDao) :
    ViewModel() {

    // Goals

    fun addNewGoal(dataName: String, dataDate: Date) {
        val newGoal = getNewGoalEntry(dataName, dataDate)
        insertGoal(newGoal)
    }

    private fun insertGoal(data: Goals) {
        viewModelScope.launch {
            goalsDao.insert(data)
        }
    }

    fun isEntryValid(dataName: String, dataDate: String): Boolean {
        if (dataName.isBlank() || dataDate.isBlank()) {
            return false
        }

        return true
    }

    private fun getNewGoalEntry(
        dataName: String,
        dataDate: Date,
    ): Goals {
        return Goals(
            dataName = dataName,
            dataDate = dataDate as java.sql.Date,
        )
    }

    // Lessons

    fun addNewLesson(lessonName: String, lessonScore: Double, goalsId: Int) {
        val newLesson = getNewLessonEntry(lessonName, lessonScore, goalsId)
        insertLesson(newLesson)
    }

    private fun insertLesson(data: Lessons) {
        viewModelScope.launch {
            lessonsDao.insert(data)
        }
    }

    fun isEntryValid(lessonName: String, lessonScore: String, goalsId: String): Boolean {
        if (lessonName.isBlank() || lessonScore.isBlank() || goalsId.isBlank()) {
            return false
        }

        return true
    }

    private fun getNewLessonEntry(lessonName: String, lessonScore: Double, goalsId: Int): Lessons {
        return Lessons(
            lessonName = lessonName,
            lessonScore = lessonScore,
            goalsId = goalsId
        )
    }
}

class AppDatabaseViewModelFactory(
    private val goalsDao: GoalsDao,
    private val lessonsDao: LessonsDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppDatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppDatabaseViewModel(goalsDao, lessonsDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}