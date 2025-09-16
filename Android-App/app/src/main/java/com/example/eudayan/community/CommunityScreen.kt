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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun CommunityScreen(navController: NavController, posts: List<Post>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(posts) { index, post ->
            PostItem(post = post, navController = navController, postIndex = index)
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