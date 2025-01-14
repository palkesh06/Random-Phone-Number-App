package com.example.randomphonenumber.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.randomphonenumber.data.db.dao.PhoneDetailDao
import com.example.randomphonenumber.data.db.model.PhoneDetail

@Database(entities = [PhoneDetail::class], version = 1)
abstract  class AppDatabase: RoomDatabase() {
    companion object{
        const val DATABASE_NAME = "phone_db"
    }
    abstract fun phoneDetailDao(): PhoneDetailDao
}