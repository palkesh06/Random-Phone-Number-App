package com.example.randomphonenumber.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomPhoneService {
    @GET("Phone/Generate")
    suspend fun getRandomPhoneNumber(
        @Query("CountryCode") countryCode: String = "IN",
        @Query("Quantity") quantity: Int = 1,
    ): Response<List<String>>
}