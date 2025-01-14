package com.example.randomphonenumber.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.randomphonenumber.data.db.model.PhoneDetail

@Dao
interface PhoneDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoneDetails(
        phoneDetails: List<PhoneDetail>,
    )

    @Query("SELECT * FROM phone_details")
    suspend fun getAllPhoneDetails(): List<PhoneDetail>

    @Query("DELETE FROM phone_details")
    suspend fun deleteAllPhoneDetails()
}