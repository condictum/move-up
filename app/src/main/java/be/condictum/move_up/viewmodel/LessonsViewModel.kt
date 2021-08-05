package be.condictum.move_up.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.condictum.move_up.database.dao.LessonsDao
import be.condictum.move_up.database.data.Lessons
import kotlinx.coroutines.launch

class LessonsViewModel(private val lessonsDao: LessonsDao) : ViewModel() {
    val allLessons: LiveData<List<Lessons>> = lessonsDao.getAllData()

    fun addNewLesson(lessonName: String, lessonScore: String, goalsId: String) {
        val newLesson = getNewLessonEntry(lessonName, lessonScore.toDouble(), goalsId.toInt())
        insertLesson(newLesson)
    }

    fun getLessonById(id: Int): Lessons {
        return lessonsDao.getLesson(id)
    }

    private fun insertLesson(data: Lessons) {
        viewModelScope.launch {
            lessonsDao.insert(data)
        }
    }

    fun updateLesson(data: Lessons) {
        viewModelScope.launch {
            lessonsDao.update(data)
        }
    }

    fun deleteLesson(data: Lessons) {
        viewModelScope.launch {
            lessonsDao.delete(data)
        }
    }

    fun isEntryValid(lessonName: String, lessonScore: String): Boolean {
        if (lessonName.isBlank() || lessonScore.isBlank()) {
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