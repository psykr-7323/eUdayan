package com.example.eudayan.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // Whole package imported
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eudayan.mood.Mood

@Composable
fun HomeScreen(showSignupSuccess: Boolean) {
    val context = LocalContext.current
    if (showSignupSuccess) {
        LaunchedEffect(showSignupSuccess) {
            Toast.makeText(context, "Signup Successful!", Toast.LENGTH_LONG).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 76.dp) 
    ) {
        item {
            Text("Hi User,", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item { MoodEntrySection() }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Mental Wellness Resources", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item { ResourceSection() }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            MindfulnessQuote()
        }
    }
}

@Composable
fun MoodEntrySection() {
    val moods = listOf(
        Mood("Happy", "😀"),
        Mood("Sad", "😞"),
        Mood("Angry", "😠"),
        Mood("Calm", "😌"),
        Mood("Anxious", "😟"),
        Mood("Tired", "😴")
    )
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    var journalEntry by remember { mutableStateOf("") }

    Column {
        Text("How are you feeling today?", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        val moodRows = moods.chunked(3)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { // Changed 8.dp to 4.dp
            moodRows.forEach { rowMoods ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    rowMoods.forEach { mood ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(RoundedCornerShape(50))
                                .clickable { selectedMood = if (selectedMood == mood) null else mood }
                                .background(
                                    if (mood == selectedMood) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else Color.Transparent
                                )
                                .padding(vertical = 5.dp, horizontal = 5.dp)
                        ) {
                            Text(mood.emoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(mood.name, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        AnimatedVisibility(visible = selectedMood != null) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = journalEntry,
                    onValueChange = { journalEntry = it },
                    label = { Text("Tell us more about your mood...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Submit Mood")
                }
            }
        }
    }
}

@Composable
fun ResourceSection() {
    val resources = listOf(
        "Meditation Videos" to "Guided meditation sessions for stress relief.",
        "Relaxation Audio" to "Calming sounds and breathing exercises.",
        "Mindful Articles" to "Tips and tricks for a healthier mind.",
        "Yoga Poses" to "Stretches to release physical tension."
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ResourceCard(title = resources[0].first, subtitle = resources[0].second, modifier = Modifier.weight(1f))
            ResourceCard(title = resources[1].first, subtitle = resources[1].second, modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ResourceCard(title = resources[2].first, subtitle = resources[2].second, modifier = Modifier.weight(1f))
            ResourceCard(title = resources[3].first, subtitle = resources[3].second, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ResourceCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(150.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, fontSize = 12.sp)
        }
    }
}

@Composable
fun MindfulnessQuote() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = "“Do not lose heart, even in darkness — the narcissus still blooms in snow.”\n– Rehman Rahi",
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
