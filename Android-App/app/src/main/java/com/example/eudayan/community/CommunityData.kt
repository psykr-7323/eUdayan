package com.example.eudayan.community

import androidx.annotation.DrawableRes
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Post(
    val author: String,
    @DrawableRes val authorImage: Int,
    val time: String,
    val views: Int,
    val content: String,
    val imageUrl: Int? = null,
    var likes: Int, // Changed to var
    val comments: SnapshotStateList<Comment>, // Already SnapshotStateList
    var isLiked: Boolean = false, // Changed to var
    val authorIsMod: Boolean = false
)

data class Comment(
    val id: String,
    val author: String,
    @DrawableRes val authorImage: Int,
    val comment: String,
    var likes: Int, // Changed to var
    val isMod: Boolean = false,
    var isLiked: Boolean = false, // Changed to var
    val replies: SnapshotStateList<Comment> = mutableStateListOf() // Already SnapshotStateList
)
