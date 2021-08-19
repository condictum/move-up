package be.condictum.move_up.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.condictum.move_up.database.dao.GoalsDao
import be.condictum.move_up.database.dao.LessonsDao
import be.condictum.move_up.database.dao.ProfilesDao
import be.condictum.move_up.database.dao.SettingsDao
import be.condictum.move_up.database.data.Profiles
import be.condictum.move_up.database.data.Settings
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsDao:SettingsDao):ViewModel(){

    val allSettings: LiveData<List<Settings>> = settingsDao.getAllData()


    fun updateSetting(data: Settings) {
        viewModelScope.launch {
            settingsDao.update(data)
        }
    }
    fun getSettingById(id: Int): Settings {
        return settingsDao.getData(id)
    }

    class SettingsViewModelFactory(
        private val settingsDao: SettingsDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfilesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(settingsDao) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}