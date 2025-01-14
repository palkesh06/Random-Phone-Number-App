package com.example.randomphonenumber.ui.phoneDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomphonenumber.data.db.model.PhoneDetail
import com.example.randomphonenumber.data.repository.PhoneNumberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneDetailsViewModel @Inject constructor(
    private val repository: PhoneNumberRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun savePhoneDetails(phoneNumbers: List<String>, tags: List<String>, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val phoneDetails = phoneNumbers.mapIndexed { index, phone ->
                val tag = if (tags[index] == "Other") {
                    "Other ${tags.take(index).count { it.startsWith("Other") } + 1}"
                } else {
                    tags[index]
                }
                PhoneDetail(phoneNumber = phone, tag = tag)
            }
            repository.savePhoneDetails(phoneDetails)
            _isLoading.value = false
            onComplete()
        }
    }
}
