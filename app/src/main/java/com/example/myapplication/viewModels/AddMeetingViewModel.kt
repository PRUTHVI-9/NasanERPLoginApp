package com.example.myapplication.viewModels

import androidx.annotation.OptIn
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.myapplication.data.model.MeetingDescResponse
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddMeetingViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val result = MutableLiveData<UiState<MeetingDescResponse>>()

    fun fetchMeetingDesc() {
        result.value = UiState.Loading
        viewModelScope.launch {
            result.value = repository.fetchMeetingDesc()
        }
    }
}

