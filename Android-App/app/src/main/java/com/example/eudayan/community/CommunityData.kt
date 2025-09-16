package com.example.eudayan.community

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Post(
    val author: String,
    val time: String,
    val views: Int,
    val content: String,
    val imageUrl: Int? = null,
    val likes: Int,
    val comments: SnapshotStateList<Comment>,
    val isLiked: Boolean = false,
    val authorIsMod: Boolean = false
)

data class Comment(
    val author: String,
    val comment: String,
    val likes: Int,
    val isMod: Boolean = false,
    val isLiked: Boolean = false,
    val replies: SnapshotStateList<Comment> = mutableStateListOf()
)
