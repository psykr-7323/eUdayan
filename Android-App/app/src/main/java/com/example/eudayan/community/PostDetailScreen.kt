package com.example.eudayan.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Send
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
    var replyTo by remember { mutableStateOf<Comment?>(null) }
    var showReplyTextField by remember { mutableStateOf(false) }
    val post = posts[postIndex]

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                PostItem(post = post, navController = navController, isDetailScreen = true)
                Row(Modifier.padding(horizontal = 12.dp)) {
                    IconButton(onClick = {
                        val updatedPost = post.copy(likes = if (post.isLiked) post.likes - 1 else post.likes + 1, isLiked = !post.isLiked)
                        posts[postIndex] = updatedPost
                    }) {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = "Like",
                            tint = if (post.isLiked) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                    Text(text = post.likes.toString())
                }
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
            itemsIndexed(post.comments) { commentIndex, comment ->
                CommentItem(comment = comment, onReply = {
                    replyTo = it
                    showReplyTextField = true
                }) { updatedComment ->
                    post.comments[commentIndex] = updatedComment
                }
            }
        }

        ReplyTextField(replyTo = replyTo, onReply = {
            if (replyTo != null) {
                val parentComment = post.comments.find { it == replyTo }
                parentComment?.replies?.add(it)
            } else {
                post.comments.add(it)
            }
            showReplyTextField = false
            replyTo = null
        })

    }
}

@Composable
fun CommentItem(comment: Comment, isReply: Boolean = false, onReply: (Comment) -> Unit, onCommentUpdate: (Comment) -> Unit) {
    Column(modifier = Modifier.padding(start = if (isReply) 32.dp else 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Author",
                modifier = Modifier.size(if (isReply) 24.dp else 32.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(comment.author, fontWeight = FontWeight.Bold, fontSize = if (isReply) 12.sp else 14.sp)
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
        Text(comment.comment, fontSize = if (isReply) 12.sp else 14.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                val updatedComment = comment.copy(likes = if (comment.isLiked) comment.likes - 1 else comment.likes + 1, isLiked = !comment.isLiked)
                onCommentUpdate(updatedComment)
            }) {
                Icon(
                    Icons.Default.ThumbUp,
                    contentDescription = "Like",
                    tint = if (comment.isLiked) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
            Text(text = comment.likes.toString())
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onReply(comment) }) {
                Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = "Reply")
            }
        }
        if (comment.replies.isNotEmpty()) {
            comment.replies.forEach { reply ->
                CommentItem(comment = reply, isReply = true, onReply = onReply, onCommentUpdate = onCommentUpdate)
            }
        }
        if (!isReply) {
            Divider(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun ReplyTextField(replyTo: Comment?, onReply: (Comment) -> Unit) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(if (replyTo != null) "Reply to ${replyTo.author}" else "Add a comment") },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            onReply(Comment("Anonymous", text, 0))
            text = ""
        }) {
            Icon(Icons.Default.Send, contentDescription = "Send")
        }
    }
}
