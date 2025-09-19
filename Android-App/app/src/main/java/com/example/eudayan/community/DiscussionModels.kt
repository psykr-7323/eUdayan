package com.example.eudayan.community

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("_id") val id: String,
    val name: String,
    val schoolInfo: String? = null
)

@Serializable
data class Reply(
    @SerialName("_id") val id: String,
    val user: UserInfo,
    val text: String,
    val createdAt: String
)

@Serializable
data class Comment(
    @SerialName("_id") val id: String,
    val user: UserInfo,
    val text: String,
    val replies: List<Reply> = emptyList(),
    val likes: List<String> = emptyList(),
    val createdAt: String
)

@Serializable
data class Discussion(
    @SerialName("_id") val id: String,
    val user: UserInfo,
    val text: String,
    val media: String? = null,
    val likes: List<String>, // List of user IDs
    val comments: List<Comment>,
    val createdAt: String
)