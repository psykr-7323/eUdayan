package com.example.eudayan.community

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.filled.ThumbDown
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

data class Reply(
    val author: String,
    @DrawableRes val authorImage: Int, // Added authorImage
    val time: String,
    val content: String,
    val likes: Int,
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepliesScreen(navController: NavController, commentLikes: Int) {
    val replies = remember {
        mutableStateListOf(
            Reply("veronica3986", R.drawable.user_image, "6 days ago", "@rulingrohan....ive never heard that marley quote b4 but it sums it up for me too", 14),
            Reply("tracym8952", R.drawable.user_image, "6 days ago", "Ask the rich man, he'll confess Money can't buy happiness Ask the poor man, he don't doubt But he'd rather be miserable w... Read more", 29),
            Reply("ajr993", R.drawable.user_image, "6 days ago", "It's a stupid quote. Nobody rich is tying their happiness to the absolute number of their wealth. They're tying it to the freedom and comfort that wealth repre... Read more", 44)
        )
    }
    var replyText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.user_image), // Changed to user_image
                            contentDescription = "Author",
                            modifier = Modifier.size(40.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("@rulingrohan • 6 days ago", fontSize = 12.sp, color = Color.Gray)
                            Text("\"Money is numbers, and numbers never end. If it takes money to be happy, your search for happiness will never end.\" — Bob Marley", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.ThumbUp, contentDescription = "Likes")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(commentLikes.toString())
                        }
                        Icon(Icons.Outlined.ThumbDown, contentDescription = "Dislikes")
                    }
                }
            }
            itemsIndexed(replies) { index, reply ->
                ReplyItem(reply = reply) { isLiked, isDisliked ->
                    var newLikes = reply.likes
                    if (isLiked) {
                        newLikes++
                    } else if (reply.isLiked) {
                        newLikes--
                    }
                    replies[index] = reply.copy(likes = newLikes, isLiked = isLiked, isDisliked = isDisliked)
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_image), // Changed to user_image
                contentDescription = "User Avatar",
                modifier = Modifier.size(40.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                placeholder = { Text("Add a reply...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                if (replyText.isNotBlank()) {
                    replies.add(0, Reply("Me", R.drawable.user_image, "now", replyText, 0)) // Added R.drawable.user_image
                    replyText = ""
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send reply"
                )
            }
        }
    }
}

@Composable
fun ReplyItem(reply: Reply, onLikeClicked: (Boolean, Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.Top) {
        Image(
            painter = painterResource(id = reply.authorImage), // Changed to reply.authorImage
            contentDescription = "Author",
            modifier = Modifier.size(40.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("@${reply.author} • ${reply.time}", fontSize = 12.sp, color = Color.Gray)
            Text(reply.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onLikeClicked(!reply.isLiked, false) }) {
                        Icon(
                            imageVector = if (reply.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                            contentDescription = "Likes",
                            tint = if (reply.isLiked) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                    Text(reply.likes.toString())
                }
                IconButton(onClick = { onLikeClicked(false, !reply.isDisliked) }) {
                    Icon(
                        imageVector = if (reply.isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                        contentDescription = "Dislikes",
                        tint = if (reply.isDisliked) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        }
    }
}
