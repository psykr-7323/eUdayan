package com.example.eudayan.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout // Changed to AutoMirrored
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch

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
    onMoodLog: () -> Unit,
    onSettingsClick: () -> Unit, 
    isNotificationsEnabled: Boolean,
    onNotificationsToggled: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Added horizontal alignment
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = R.drawable.user_image),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        androidx.compose.material3.Text("User", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        androidx.compose.material3.Text("username@mail.com", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(32.dp))
        androidx.compose.material3.HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))


        DrawerItem(
            icon = Icons.Default.Notifications,
            text = "Notifications",
            onClick = { onNotificationsToggled(!isNotificationsEnabled) },
            trailingContent = {
                Switch(
                    checked = isNotificationsEnabled,
                    onCheckedChange = onNotificationsToggled
                )
            }
        )
        DrawerItem(icon = Icons.Default.Settings, text = "Settings", onClick = onSettingsClick)
        DrawerItem(icon = Icons.Default.Mood, text = "Mood Log", onClick = onMoodLog)
        DrawerItem(icon = Icons.AutoMirrored.Filled.Logout, text = "Sign Out", onClick = onSignOut) // Changed here
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        androidx.compose.material3.Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.padding(end = 16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        androidx.compose.material3.Text(text, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
        if (trailingContent != null) {
            trailingContent()
        }
    }
}
