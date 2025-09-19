package com.example.eudayan.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Changed from itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eudayan.R

@Composable
fun PostDetailScreen(navController: NavController, posts: MutableList<Post>, postIndex: Int) {
    val post = posts[postIndex]

    var replyToComment by remember { mutableStateOf<Comment?>(null) }
    var showReplyTextFieldForPost by remember { mutableStateOf(false) }

    val showActualReplyTextField = showReplyTextFieldForPost || (replyToComment != null)

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = if (showActualReplyTextField) 0.dp else 76.dp)
        ) {
            item {
                PostItem( // Assumes PostItem is defined in CommunityScreen.kt and handles its display
                    post = post,
                    navController = navController, // Only if PostItem needs it for non-detail navigation
                    isDetailScreen = true,
                    isLiked = post.isLiked, // Pass the current like state
                    onLikeClicked = {
                        if (post.isLiked) {
                            post.likes--
                        } else {
                            post.likes++
                        }
                        post.isLiked = !post.isLiked
                        posts[postIndex] = post.copy() // Create a new instance to ensure recomposition
                    },
                    onReplyClicked = {
                        replyToComment = null
                        showReplyTextFieldForPost = true
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(post.comments, key = { it.id }) { comment -> // Use comment.id as a stable key
                CommentItem(
                    comment = comment,
                    onReplyToThisComment = {
                        replyToComment = it
                        showReplyTextFieldForPost = false
                    }
                )
            }
        }

        if (showActualReplyTextField) {
            ReplyTextField(
                replyToAuthor = replyToComment?.author,
                onReplySent = { newCommentText ->
                    val newId = System.currentTimeMillis().toString()
                    val newCommentInstance = Comment(
                        id = newId,
                        author = "Me", // Replace with actual user
                        authorImage = R.drawable.user_image, // Replace with actual user image
                        comment = newCommentText,
                        likes = 0
                        // isMod, isLiked defaults to false, replies defaults to empty SnapshotStateList
                    )

                    if (replyToComment != null) {
                        replyToComment?.replies?.add(newCommentInstance)
                    } else {
                        post.comments.add(newCommentInstance)
                    }
                    replyToComment = null
                    showReplyTextFieldForPost = false
                },
                onDismiss = {
                    replyToComment = null
                    showReplyTextFieldForPost = false
                }
            )
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    isReplyVisual: Boolean = false, // To control visual indentation for replies
    onReplyToThisComment: (Comment) -> Unit // Callback when "reply" on *this* comment is clicked
) {
    Column(
        modifier = Modifier.padding(
            start = if (isReplyVisual) 32.dp else 12.dp,
            end = 12.dp,
            top = 8.dp,
            bottom = 8.dp
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = comment.authorImage),
                contentDescription = "Author",
                modifier = Modifier.size(if (isReplyVisual) 24.dp else 32.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(comment.author, fontWeight = FontWeight.Bold, fontSize = if (isReplyVisual) 12.sp else 14.sp)
                if (comment.isMod) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text("Mod", color = Color.White, fontSize = 8.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(comment.comment, fontSize = if (isReplyVisual) 12.sp else 14.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                if (comment.isLiked) {
                    comment.likes--
                } else {
                    comment.likes++
                }
                comment.isLiked = !comment.isLiked
            }) {
                Icon(
                    Icons.Default.ThumbUp,
                    contentDescription = "Like",
                    tint = if (comment.isLiked) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
            Text(text = comment.likes.toString())
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onReplyToThisComment(comment) }) {
                Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = "Reply to Comment")
            }
        }

        if (comment.replies.isNotEmpty()) {
            comment.replies.forEach { reply ->
                CommentItem( // Recursive call for replies
                    comment = reply,
                    isReplyVisual = true, // Indicate it's a reply for visual styling
                    onReplyToThisComment = onReplyToThisComment // Pass down the same reply handler
                )
            }
        }
        if (!isReplyVisual) { 
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun ReplyTextField(
    replyToAuthor: String?, // Just the author's name for the label
    onReplySent: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(if (replyToAuthor != null) "Reply to $replyToAuthor" else "Add a comment...") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                if (text.isNotBlank()) {
                    onReplySent(text)
                    text = ""
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}
