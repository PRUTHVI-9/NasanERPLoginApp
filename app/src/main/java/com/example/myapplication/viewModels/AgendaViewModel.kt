package com.example.myapplication.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.myapplication.data.model.AgendaResponse
import com.example.myapplication.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import retrofit2.Response


@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _agenda = MutableLiveData<AgendaResponse>()
    val agenda: LiveData<AgendaResponse> get() = _agenda

    fun fetchAgenda(meetingDescId: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchAgenda(meetingDescId)
                if (response.isSuccessful) {
                    _agenda.postValue(response.body())
                } else {
                    _agenda.postValue(AgendaResponse(emptyList()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


