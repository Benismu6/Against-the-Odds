package edu.towson.cosc435.basaran.againsttheodds.models

import edu.towson.cosc435.basaran.againsttheodds.repository.AthleteRepository
import edu.towson.cosc435.basaran.againsttheodds.data.Athlete
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AthleteViewModel : ViewModel() {

    // LiveData to hold the list of athletes
    private val _athleteList = MutableLiveData<List<Athlete>>()
    val athleteList: LiveData<List<Athlete>> get() = _athleteList

    fun fetchAndUploadData() {
        viewModelScope.launch {
            AthleteRepository.fetchAndUploadData { athletes: List<Athlete> ->
                // Update the LiveData with the fetched list of athletes
                _athleteList.postValue(athletes)
            }
        }
    }
}
