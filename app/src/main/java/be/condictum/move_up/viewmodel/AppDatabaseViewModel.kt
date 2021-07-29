package be.condictum.move_up.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.condictum.move_up.database.AppDao
import be.condictum.move_up.database.AppData
import kotlinx.coroutines.launch

class AppDatabaseViewModel(private val appDao: AppDao) : ViewModel() {
    fun addNewData(dataName: String, dataPrice: String, dataCount: String) {
        val newData = getNewDataEntry(dataName, dataPrice, dataCount)
        insertData(newData)
    }

    private fun insertData(data: AppData) {
        viewModelScope.launch {
            appDao.insert(data)
        }
    }

    fun isEntryValid(dataName: String, dataPrice: String, dataCount: String): Boolean {
        if (dataName.isBlank() || dataPrice.isBlank() || dataCount.isBlank()) {
            return false
        }

        return true
    }

    private fun getNewDataEntry(dataName: String, dataPrice: String, dataCount: String): AppData {
        return AppData(
            dataName = dataName,
            dataPrice = dataPrice.toDouble(),
            dataCount = dataCount.toInt()
        )
    }
}

class AppDatabaseViewModelFactory(private val appDao: AppDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppDatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppDatabaseViewModel(appDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}