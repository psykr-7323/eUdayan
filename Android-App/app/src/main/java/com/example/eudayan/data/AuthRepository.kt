package com.example.eudayan.data

import com.example.eudayan.network.ApiService
import com.example.eudayan.network.LoginRequest
import com.example.eudayan.network.SignupRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun signup(signupRequest: SignupRequest) = apiService.signup(signupRequest)

    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)

}
