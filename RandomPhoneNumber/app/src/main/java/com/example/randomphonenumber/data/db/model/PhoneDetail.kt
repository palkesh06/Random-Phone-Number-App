package com.example.randomphonenumber.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phone_details")
data class PhoneDetail (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phoneNumber: String,
    val tag: String
)