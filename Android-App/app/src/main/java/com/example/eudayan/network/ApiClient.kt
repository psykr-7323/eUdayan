package com.example.eudayan.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object ApiClient {

    // Replace with your computer's local IP address, not localhost.
    // Your phone and computer must be on the same Wi-Fi network.
    private const val BASE_URL = "http://192.168.1.10:5000/" // <-- IMPORTANT: Change this

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}