package com.example.dedclick.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.dedclick.BuildConfig

object RetrofitProvider {

    private const val BASE_URL = BuildConfig.BASE_URL
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}