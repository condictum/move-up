package be.condictum.move_up.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.condictum.move_up.database.dao.GoalsDao
import be.condictum.move_up.database.data.Goals
import kotlinx.coroutines.launch
import java.sql.Date

class GoalsViewModel(private val goalsDao: GoalsDao) : ViewModel() {
    fun getAllLiveDataByProfileId(profileId: Int): LiveData<List<Goals>> {
        return goalsDao.getAllLiveDataByProfileId(profileId)
    }

    fun getAllDataByProfileId(profileId: Int): List<Goals> {
        return goalsDao.getAllDataByProfileId(profileId)
    }

    fun addNewGoal(dataName: String, dataDate: Date, profilesId: Int) {
        val newGoal = getNewGoalEntry(dataName, dataDate, profilesId)
        insertGoal(newGoal)
    }

    fun deleteProfileById(data: Goals) {
        viewModelScope.launch {
            goalsDao.delete(data)
        }
    }

    private fun insertGoal(data: Goals) {
        viewModelScope.launch {
            goalsDao.insert(data)
        }
    }

    fun updateProfile(data: Goals) {
        viewModelScope.launch {
            goalsDao.update(data)
        }
    }

    fun isEntryValid(dataName: String, dataDate: String): Boolean {
        if (dataName.isBlank() || dataDate.isBlank() || dataName !is String ) {
            return false
        }

        return true
    }

    private fun getNewGoalEntry(
        dataName: String,
        dataDate: Date,
        profilesId: Int
    ): Goals {
        return Goals(
            dataName = dataName,
            dataDate = dataDate,
            profilesId = profilesId,
        )
    }
}

class GoalsViewModelFactory(
    private val goalsDao: GoalsDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoalsViewModel(goalsDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}