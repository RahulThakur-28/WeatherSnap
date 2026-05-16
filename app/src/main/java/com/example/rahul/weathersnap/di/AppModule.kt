package com.example.rahul.weathersnap.di

import android.content.Context
import androidx.room.Room
import com.example.rahul.weathersnap.data.local.ReportDao
import com.example.rahul.weathersnap.data.local.WeatherDatabase
import com.example.rahul.weathersnap.data.remote.WeatherApi
import com.example.rahul.weathersnap.data.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideReportDao(db: WeatherDatabase): ReportDao {
        return db.reportDao
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi,
        dao: ReportDao
    ): WeatherRepositoryImpl {
        return WeatherRepositoryImpl(api, dao)
    }
}
