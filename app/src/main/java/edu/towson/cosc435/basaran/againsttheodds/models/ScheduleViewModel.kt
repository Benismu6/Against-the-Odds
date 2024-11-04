package edu.towson.cosc435.basaran.againsttheodds.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.towson.cosc435.basaran.againsttheodds.data.ScheduleResponse
import edu.towson.cosc435.basaran.againsttheodds.repository.ScheduleRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ScheduleViewModel : ViewModel() {

    private val _scheduleData = MutableLiveData<ScheduleResponse?>()
    val scheduleData: LiveData<ScheduleResponse?> get() = _scheduleData

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val repository = ScheduleRepository

    fun fetchAndUploadData() {
        viewModelScope.launch {
            try {
                val scheduleResponse = repository.fetchAllSchedules()
                _scheduleData.postValue(scheduleResponse)
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to fetch schedule: ${e.message}")
            }
        }
    }
}
