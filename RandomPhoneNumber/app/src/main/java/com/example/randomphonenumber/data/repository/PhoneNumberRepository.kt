package com.example.randomphonenumber.data.repository

import android.util.Log
import com.example.randomphonenumber.data.db.dao.PhoneDetailDao
import com.example.randomphonenumber.data.db.model.PhoneDetail
import com.example.randomphonenumber.data.retrofit.RandomPhoneService
import javax.inject.Inject

class PhoneNumberRepository @Inject constructor(
    private val phoneService: RandomPhoneService,
    private val phoneDetailDao: PhoneDetailDao
){
    suspend fun fetchRandomPhoneNumber(countryCode: String): String? {
        return try {
            val response = phoneService.getRandomPhoneNumber(countryCode)
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body()?.get(0)
            } else {
                Log.e("PhoneNumberRepository", "Unsuccessful response: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PhoneNumberRepository", "Error fetching phone number", e)
            null
        }
    }

    suspend fun savePhoneDetails(phoneDetails: List<PhoneDetail>) {
        phoneDetailDao.insertPhoneDetails(phoneDetails)
    }

    suspend fun getAllPhoneDetails(): List<PhoneDetail> {
        return phoneDetailDao.getAllPhoneDetails()
    }

    suspend fun clearPhoneDetails() {
        phoneDetailDao.deleteAllPhoneDetails()
    }


}