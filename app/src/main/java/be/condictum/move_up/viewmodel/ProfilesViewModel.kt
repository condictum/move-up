package be.condictum.move_up.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.condictum.move_up.database.dao.ProfilesDao
import be.condictum.move_up.database.data.Profiles
import kotlinx.coroutines.launch


class ProfilesViewModel(private val profilesDao: ProfilesDao) : ViewModel() {
    val allProfiles: LiveData<List<Profiles>> = profilesDao.getAllData()

    fun addNewProfile(name: String, surname: String, age: Int) {
        val newProfile = getNewProfileEntry(name, surname, age)
        insertProfile(newProfile)
    }

    fun getProfileById(id: Int): Profiles {
        return profilesDao.getData(id)
    }

    private fun insertProfile(data: Profiles) {
        viewModelScope.launch {
            profilesDao.insert(data)
        }
    }

    fun updateProfile(data: Profiles) {
        viewModelScope.launch {
            profilesDao.update(data)
        }
    }

    fun deleteProfile(data: Profiles) {
        viewModelScope.launch {
            profilesDao.delete(data)
        }
    }

    fun isEntryValid(name: String, surname: String, age: String): Boolean {
        if (name.isBlank() || surname.isBlank() || age.isBlank()) {
            return false
        }

        return true
    }

    private fun getNewProfileEntry(
        name: String, surname: String, age: Int
    ): Profiles {
        return Profiles(
            name = name,
            surname = surname,
            age = age,
        )
    }
}

class ProfilesViewModelFactory(
    private val profilesDao: ProfilesDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfilesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfilesViewModel(profilesDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}