package com.example.eudayan.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/auth/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<AuthResponse>

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>
}

