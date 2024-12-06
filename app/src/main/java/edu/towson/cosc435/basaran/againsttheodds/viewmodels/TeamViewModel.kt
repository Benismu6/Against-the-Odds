package edu.towson.cosc435.basaran.againsttheodds.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.towson.cosc435.basaran.againsttheodds.data.TeamDescription
import edu.towson.cosc435.basaran.againsttheodds.repository.TeamRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for managing team data.
 *
 * Responsibilities:
 * - Exposes team data to the UI using LiveData.
 * - Delegates data fetching and database upload tasks to the repository.
 */
class TeamViewModel : ViewModel() {

    // LiveData to hold the list of teams
    private val _teamList = MutableLiveData<List<TeamDescription>>()
    val teamList: LiveData<List<TeamDescription>> get() = _teamList

    /**
     * Fetches team data and updates LiveData.
     *
     * Delegates the data fetching task to [TeamRepository] and updates [_teamList] upon success.
     */
    fun fetchAndUploadData() {
        viewModelScope.launch {
            TeamRepository.fetchAndUploadData { teams ->
                _teamList.postValue(teams)
            }
        }
    }
}
