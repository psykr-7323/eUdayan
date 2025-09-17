package com.example.eudayan.network

import com.example.eudayan.community.Discussion
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/discussions")
    suspend fun getDiscussions(): List<Discussion>

    @GET("api/discussions/{id}")
    suspend fun getDiscussionById(@Path("id") id: String): Discussion
}