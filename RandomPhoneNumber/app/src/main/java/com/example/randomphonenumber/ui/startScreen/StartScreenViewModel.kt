package com.example.randomphonenumber.ui.startScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomphonenumber.data.repository.ConnectivityRepository
import com.example.randomphonenumber.data.repository.PhoneNumberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val repository: PhoneNumberRepository,
    connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading= _isLoading.asStateFlow()

    val isConnected = connectivityRepository.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = false
    )

    fun fetchPhoneNumbers(onNavigate: (List<String>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val numbers = withContext(Dispatchers.IO) {
                // 4 parallel requests
                listOf(
                    async { repository.fetchRandomPhoneNumber("IN") },
                    async { repository.fetchRandomPhoneNumber("IN") },
                    async { repository.fetchRandomPhoneNumber("IN") },
                    async { repository.fetchRandomPhoneNumber("IN") }
                ).awaitAll().filterNotNull()
            }
            _isLoading.value = false
            if (numbers.isNotEmpty()) {
                onNavigate(numbers)
                viewModelScope.launch(Dispatchers.IO) {
                    repository.clearPhoneDetails() // Assuming this calls deleteAllPhoneDetails() in DAO
                    Log.d("PhoneNumberViewModel", "Cleared phone details from database")
                }
            }
            Log.d("PhoneNumberViewModel", "Fetched phone numbers: $numbers")
        }
    }
}