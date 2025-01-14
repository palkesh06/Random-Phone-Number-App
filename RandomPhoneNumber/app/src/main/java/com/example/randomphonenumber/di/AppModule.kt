package com.example.randomphonenumber.di

import android.content.Context
import androidx.room.Room
import com.example.randomphonenumber.data.db.AppDatabase
import com.example.randomphonenumber.data.db.AppDatabase.Companion.DATABASE_NAME
import com.example.randomphonenumber.data.repository.ConnectivityRepository
import com.example.randomphonenumber.data.retrofit.RandomPhoneService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    companion object {
        private const val BASE_URL = "https://randommer.io/api/"
        private const val API_KEY = "68742b10dbc14eae8a19acd6511c2500"

        @Singleton
        @Provides
        fun provideConnectivityRepository(
            @ApplicationContext context: Context
        ): ConnectivityRepository {
            return ConnectivityRepository(context)
        }

        @Provides
        @Singleton
        fun provideAppDatabase(
            @ApplicationContext context: Context
        ) : AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()

        @Provides
        @Singleton
        fun providePhoneDetailDao(
            appDatabase: AppDatabase
        ) = appDatabase.phoneDetailDao()

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("X-Api-Key", API_KEY)
                        .build()
                    chain.proceed(request)
                }
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(
            okHttpClient: OkHttpClient
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun providePhoneService(
            retrofit: Retrofit
        ): RandomPhoneService {
            return retrofit.create(RandomPhoneService::class.java)
        }
    }
}