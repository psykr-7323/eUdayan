package com.example.eudayan.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eudayan.R
import com.example.eudayan.mood.Mood

@Composable
fun HomeScreen(showSignupSuccess: Boolean) {
    val context = LocalContext.current
    var moodSubmittedToday by remember { mutableStateOf(false) } // State for submission status

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

        item {
            if (moodSubmittedToday) {
                Text(
                    text = "You saved your today's Mood Log",
                    style = MaterialTheme.typography.titleLarge, // Using a slightly larger style
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp) // Add some padding for better appearance
                )
            } else {
                MoodEntrySection(onMoodSubmitted = { mood, journal ->
                    // This is where you would typically save the mood and journal to a database or backend,
                    // and then navigate to the Mood Log screen.
                    // For now, we'll just update the UI state.
                    moodSubmittedToday = true
                    // TODO: Add navigation to Mood Log Screen, passing mood and journal
                })
            }
        }

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
fun MoodEntrySection(onMoodSubmitted: (mood: Mood, journal: String) -> Unit) { // Modified callback
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
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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

        AnimatedVisibility(
            visible = selectedMood != null,
            enter = fadeIn(animationSpec = tween(durationMillis = 0)),
            exit = fadeOut(animationSpec = tween(durationMillis = 0))
        ) {
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
                    onClick = {
                        // TODO: Implement actual data saving logic here (e.g., to ViewModel/Repository)
                        // For example: viewModel.saveMood(selectedMood, journalEntry)

                        // Pass data to callback
                        if (selectedMood != null) {
                            onMoodSubmitted(selectedMood!!, journalEntry)
                        }

                        // Clear local states
                        selectedMood = null
                        journalEntry = ""
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = selectedMood != null // Optional: Disable button if no mood is selected
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
            ResourceCard(
                title = resources[0].first,
                subtitle = resources[0].second,
                modifier = Modifier.weight(1f),
                backgroundImageRes = R.drawable.meditation
            )
            ResourceCard(
                title = resources[1].first,
                subtitle = resources[1].second,
                modifier = Modifier.weight(1f),
                backgroundImageRes = R.drawable.audio
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ResourceCard(
                title = resources[2].first,
                subtitle = resources[2].second,
                modifier = Modifier.weight(1f),
                backgroundImageRes = R.drawable.article
            )
            ResourceCard(
                title = resources[3].first,
                subtitle = resources[3].second,
                modifier = Modifier.weight(1f),
                backgroundImageRes = R.drawable.yoga
            )
        }
    }
}

@Composable
fun ResourceCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    backgroundImageRes: Int? = null
) {
    Card(
        modifier = modifier.height(150.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (backgroundImageRes != null) {
                Image(
                    painter = painterResource(id = backgroundImageRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.2f),
                                    Color.Black.copy(alpha = 0.8f),
                                    Color.Black
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = if (backgroundImageRes != null) Color.White else LocalContentColor.current
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = if (backgroundImageRes != null) Color.White else LocalContentColor.current
                )
            }
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
