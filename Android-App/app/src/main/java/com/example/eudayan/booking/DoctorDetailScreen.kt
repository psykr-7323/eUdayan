package com.example.eudayan.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape // Added import for RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // Added import for ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DoctorDetailScreen(doctor: Doctor) {
    var selectedSlot by remember { mutableStateOf<String?>(null) }
    val timeSlots = listOf("10:00 AM", "11:00 AM", "12:00 PM", "02:00 PM", "03:00 PM")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = doctor.image),
            contentDescription = doctor.name,
            contentScale = ContentScale.Crop, // Added ContentScale.Crop
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(12.dp)) // Changed to RoundedCornerShape
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(doctor.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            (1..5).forEach { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = if (index <= doctor.rating.roundToInt()) Color(0xFFFFC107) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(doctor.description, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Available Slots", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            timeSlots.forEach { slot ->
                val isSelected = selectedSlot == slot
                OutlinedButton(
                    onClick = { selectedSlot = slot },
                    modifier = Modifier.padding(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(slot)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (selectedSlot != null) {
            Button(
                onClick = { /* Handle booking */ },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Book for $selectedSlot")
            }
        }
    }
}
