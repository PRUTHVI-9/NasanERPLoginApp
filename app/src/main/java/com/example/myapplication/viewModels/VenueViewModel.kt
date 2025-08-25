package com.example.myapplication.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.VenueResponse
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val venueResult = MutableLiveData<UiState<VenueResponse>>()

    fun fetchVenueList() {
        venueResult.value = UiState.Loading
        viewModelScope.launch {
            venueResult.value = repository.fetchVenueList()
        }
    }
}
