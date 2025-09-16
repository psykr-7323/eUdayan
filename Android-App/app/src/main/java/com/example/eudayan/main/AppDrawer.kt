package com.example.eudayan.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eudayan.R

@Composable
fun AppDrawer(
    onSignOut: () -> Unit,
    onMoodLog: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Om Anand", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Pre-Final Year Student", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))
        Divider()

        DrawerItem(icon = Icons.Default.Notifications, text = "Notifications", onClick = {})
        DrawerItem(icon = Icons.Default.Settings, text = "Settings", onClick = {})
        DrawerItem(icon = Icons.Default.Mood, text = "Mood Log", onClick = onMoodLog)

        DrawerItem(icon = Icons.Default.Logout, text = "Sign Out", onClick = onSignOut)
    }
}

@Composable
private fun DrawerItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(icon, contentDescription = text, modifier = Modifier.padding(end = 16.dp))
            Text(text)
        }
    }
}
