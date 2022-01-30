package com.mousavi.hashem.weatherapp.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.mousavi.hashem.weatherapp.BuildConfig
import com.mousavi.hashem.weatherapp.data.StringProvider
import com.mousavi.hashem.weatherapp.data.StringProviderImpl
import com.mousavi.hashem.weatherapp.data.local.Preferences
import com.mousavi.hashem.weatherapp.data.local.Preferences.Companion.NAME_OF_SHARED_PREF
import com.mousavi.hashem.weatherapp.data.local.PreferencesImpl
import com.mousavi.hashem.weatherapp.data.remote.Api
import com.mousavi.hashem.weatherapp.data.remote.Api.Companion.BASE_URL
import com.mousavi.hashem.weatherapp.data.remote.NetworkDataSource
import com.mousavi.hashem.weatherapp.data.remote.NetworkDataSourceImpl
import com.mousavi.hashem.weatherapp.data.repository.WeatherRepositoryImpl
import com.mousavi.hashem.weatherapp.domain.repository.WeatherRepository
import com.mousavi.hashem.weatherapp.domain.usecase.GetTomorrowWeatherUseCase
import com.mousavi.hashem.weatherapp.domain.usecase.GetTomorrowWeatherUseCaseImpl
import com.mousavi.hashem.weatherapp.domain.usecase.SetCityUseCase
import com.mousavi.hashem.weatherapp.domain.usecase.SetCityUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("okHttpLog", message)
        }.apply {
            setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            )
        }

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitApi(client: OkHttpClient): Api {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Provides
    fun provideStringProvider(@ApplicationContext appContext: Context): StringProvider {
        return StringProviderImpl(appContext)
    }

    @Provides
    @Singleton
    fun provideNetworkDataSource(networkDataSource: NetworkDataSourceImpl): NetworkDataSource {
        return networkDataSource
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(NAME_OF_SHARED_PREF, Context.MODE_PRIVATE)
    }

    @Provides
    fun providePreferences(preferences: PreferencesImpl): Preferences {
        return preferences
    }

    @Provides
    fun provideGetTomorrowWeatherUseCase(useCase: GetTomorrowWeatherUseCaseImpl): GetTomorrowWeatherUseCase {
        return useCase
    }

    @Provides
    fun provideSetCityUseCase(useCase: SetCityUseCaseImpl): SetCityUseCase {
        return useCase
    }

    @Provides
    fun provideRepository(repository: WeatherRepositoryImpl): WeatherRepository {
        return repository
    }

}