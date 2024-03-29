package be.condictum.move_up.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.condictum.move_up.data.local.dao.GoalsDao
import be.condictum.move_up.data.local.model.Goals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(private val goalsDao: GoalsDao) : ViewModel() {
    fun getAllLiveDataByProfileId(profileId: Int): LiveData<List<Goals>> {
        return goalsDao.getAllLiveDataByProfileId(profileId)
    }

    fun getAllGoals(): LiveData<List<Goals>> {
        return goalsDao.getAllGoals()
    }

    fun getAllDataByProfileId(profileId: Int): LiveData<List<Goals>> {
        return goalsDao.getAllDataByProfileId(profileId)
    }

    fun addNewGoal(dataName: String, dataDate: Date, profilesId: Int) {
        val newGoal = getNewGoalEntry(dataName, dataDate, profilesId)
        insertGoal(newGoal)
    }

    fun deleteGoal(data: Goals) {
        viewModelScope.launch(Dispatchers.IO) {
            goalsDao.delete(data)
        }
    }

    private fun insertGoal(data: Goals) {
        viewModelScope.launch(Dispatchers.IO) {
            goalsDao.insert(data)
        }
    }

    fun updateGoal(data: Goals) {
        viewModelScope.launch(Dispatchers.IO) {
            goalsDao.update(data)
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