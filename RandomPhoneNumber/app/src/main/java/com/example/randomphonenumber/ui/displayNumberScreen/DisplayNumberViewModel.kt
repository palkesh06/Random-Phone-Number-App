package com.example.randomphonenumber.ui.displayNumberScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomphonenumber.data.db.model.PhoneDetail
import com.example.randomphonenumber.data.repository.PhoneNumberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DisplayNumberViewModel @Inject constructor(
    private val repository: PhoneNumberRepository
) : ViewModel() {

    private val _phoneNumbers = MutableStateFlow<List<PhoneDetail>>(emptyList())
    val phoneNumbers: StateFlow<List<PhoneDetail>> = _phoneNumbers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchPhoneNumbers(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val phoneDetails  = withContext(Dispatchers.IO){
                    repository.getAllPhoneDetails()
                }
                _phoneNumbers.value = phoneDetails
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }
}