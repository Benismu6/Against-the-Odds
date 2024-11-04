/**
 * TeamViewModel.kt
 *
 * This file defines the TeamViewModel class, which is responsible for managing
 * the UI-related data for the Team. It extends the ViewModel class from Android's
 * Architecture Components to handle the lifecycle of the data efficiently.
 *
 * Properties:
 *
 * @property _teamList A MutableLiveData object that holds a list of Team objects.
 * @property teamList A LiveData object that exposes the _teamList for observing changes.
 *
 * Methods:
 *
 * @function fetchAndUploadData()
 * Triggers the data fetching process from the DataRepository. Once the data is
 * fetched, it updates the _teamList with the new list of teams using postValue(),
 * which ensures that the value is set on the main thread, allowing observers to
 * react to the changes.
 */
package edu.towson.cosc435.basaran.againsttheodds.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.towson.cosc435.basaran.againsttheodds.data.Team
import edu.towson.cosc435.basaran.againsttheodds.repository.TeamRepository

class TeamViewModel : ViewModel() {

    private val _teamList = MutableLiveData<List<Team>>()
    val teamList: LiveData<List<Team>> get() = _teamList

    fun fetchAndUploadData() {
        TeamRepository.fetchAndUploadData { teams ->
            _teamList.postValue(teams)
        }
    }
}
