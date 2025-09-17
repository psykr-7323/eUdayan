package com.example.eudayan.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.eudayan.main.BottomBarScreen
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun CommunityScreen(navController: NavController, viewModel: CommunityViewModel) {
    val discussions by viewModel.discussions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(text = error!!, color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(discussions, key = { it.id }) { discussion ->
                    DiscussionListItem(discussion = discussion, navController = navController)
                }
            }
        }
    }
}

@Composable
fun PostItem(post: Post, navController: NavController, isDetailScreen: Boolean = false, postIndex: Int = -1) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isDetailScreen) {
                if (postIndex != -1) {
                    navController.navigate("${BottomBarScreen.PostDetail.route}/$postIndex")
                }
            }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Author",
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(post.author, fontWeight = FontWeight.Bold)
                        if (post.authorIsMod) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Mod", color = Color.White, fontSize = 10.sp)
                            }
                        }
                    }
                    Text("${post.time} • ${post.views} views", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(post.content, style = MaterialTheme.typography.bodyMedium)

            post.imageUrl?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "Post image",
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }

            if (!isDetailScreen) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.ThumbUp, contentDescription = "Likes")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(post.likes.toString())
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Comment, contentDescription = "Comments")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(post.comments.size.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun DiscussionListItem(discussion: Discussion, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("${BottomBarScreen.PostDetail.route}/${discussion.id}")
            }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Placeholder
                    contentDescription = "Author",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(discussion.user.name, fontWeight = FontWeight.Bold)
                        // A real app would check a role property. This is a placeholder.
                        if (discussion.user.name.contains("Admin", ignoreCase = true)) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Mod", color = Color.White, fontSize = 10.sp)
                            }
                        }
                    }
                    Text(formatCreatedAt(discussion.createdAt), fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(discussion.text, style = MaterialTheme.typography.bodyMedium)

            discussion.media?.let {
                Spacer(modifier = Modifier.height(8.dp))
                // In a real app, use a library like Coil to load the image from the URL
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Post image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.ThumbUp, contentDescription = "Likes")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(discussion.likes.size.toString())
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Comment, contentDescription = "Comments")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(discussion.comments.size.toString())
                }
            }
        }
    }
}

private fun formatCreatedAt(createdAt: String): String {
    return try {
        val instant = Instant.parse(createdAt)
        val now = Instant.now()
        val duration = java.time.Duration.between(instant, now)

        when {
            duration.toDays() > 0 -> "${duration.toDays()}d ago"
            duration.toHours() > 0 -> "${duration.toHours()}h ago"
            duration.toMinutes() > 0 -> "${duration.toMinutes()}m ago"
            else -> "Just now"
        }
    } catch (e: Exception) {
        // Fallback for unexpected date format
        createdAt.split("T").firstOrNull() ?: createdAt
    }
}