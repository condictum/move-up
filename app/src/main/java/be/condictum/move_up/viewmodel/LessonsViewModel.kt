package be.condictum.move_up.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.condictum.move_up.database.dao.LessonsDao
import be.condictum.move_up.database.data.Lessons
import kotlinx.coroutines.launch

class LessonsViewModel(private val lessonsDao: LessonsDao) : ViewModel() {
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

class LessonsViewModelFactory(
    private val lessonsDao: LessonsDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LessonsViewModel(lessonsDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}