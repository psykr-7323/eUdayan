package com.example.eudayan.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun MoodLogScreen() {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val moodEntries = remember {
        
        listOf(
            MoodEntry(LocalDate.now().withDayOfMonth(12), Mood("Happy", "😀"), "Had a great day at college!"),
            MoodEntry(LocalDate.now().withDayOfMonth(13), Mood("Happy", "😀"), ""),
            MoodEntry(LocalDate.now().withDayOfMonth(15), Mood("Sad", "😞"), "Feeling a bit down today."),
            MoodEntry(LocalDate.now().withDayOfMonth(17), Mood("Happy", "😀"), "Passed my exam!"),
            // Added new entries
            MoodEntry(LocalDate.now().withDayOfMonth(5), Mood("Calm", "😌"), "Peaceful morning meditation."),
            MoodEntry(LocalDate.now().withDayOfMonth(8), Mood("Anxious", "😟"), "Worried about upcoming presentation."),
            MoodEntry(LocalDate.now().withDayOfMonth(20), Mood("Tired", "😴"), ""),
            MoodEntry(LocalDate.now().withDayOfMonth(22), Mood("Angry", "😠"), "Frustrated with traffic today."),
            MoodEntry(LocalDate.now().minusMonths(1).withDayOfMonth(28), Mood("Happy", "😀"), "Weekend getaway was fun!"),
            MoodEntry(LocalDate.now().withDayOfMonth(2), Mood("Sad", "😞"), "Missing my family.")
        )
    }
    var selectedMoodEntry by remember { mutableStateOf<MoodEntry?>(moodEntries.find { it.date == LocalDate.now() }) }

    Column(modifier = Modifier.padding(16.dp)) {
        MonthHeader(currentMonth = currentMonth, onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) }, onNextMonth = { currentMonth = currentMonth.plusMonths(1) })
        Spacer(modifier = Modifier.height(16.dp))
        CalendarView(yearMonth = currentMonth, moodEntries = moodEntries) { day ->
            selectedMoodEntry = moodEntries.find { it.date.dayOfMonth == day && it.date.month == currentMonth.month && it.date.year == currentMonth.year }
        }
        Spacer(modifier = Modifier.height(24.dp))
        selectedMoodEntry?.let {
            MoodDetailCard(moodEntry = it)
        }
    }
}

@Composable
fun MonthHeader(currentMonth: YearMonth, onPreviousMonth: () -> Unit, onNextMonth: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
        }
        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
        }
    }
}

@Composable
fun CalendarView(yearMonth: YearMonth, moodEntries: List<MoodEntry>, onDateSelected: (Int) -> Unit) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value % 7 // Ensure Monday is 0

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                Text(text = day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 0 until firstDayOfMonth) {
                item { Box(modifier = Modifier.size(56.dp)) } 
            }
            items((1..daysInMonth).toList()) { day ->
                val entry = moodEntries.find { it.date.dayOfMonth == day && it.date.month == yearMonth.month && it.date.year == yearMonth.year }
                DayCell(day = day, moodEntry = entry, onDateSelected = onDateSelected)
            }
        }
    }
}

@Composable
fun DayCell(day: Int, moodEntry: MoodEntry?, onDateSelected: (Int) -> Unit) {
    val isToday = LocalDate.now().dayOfMonth == day && LocalDate.now().monthValue == YearMonth.now().monthValue && LocalDate.now().year == YearMonth.now().year
    Box(
        modifier = Modifier
            .size(56.dp) 
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(if (isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onDateSelected(day) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center date and emoji area vertically
        ) {
            Text(text = day.toString(), fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .height(18.dp) // Fixed height for emoji area
                    .fillMaxWidth(), // Allow emoji to center
                contentAlignment = Alignment.Center // Center emoji in this box
            ) {
                if (moodEntry != null) {
                    Text(text = moodEntry.mood.emoji, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun MoodDetailCard(moodEntry: MoodEntry) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = moodEntry.date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = moodEntry.mood.emoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "You were feeling ${moodEntry.mood.name}", style = MaterialTheme.typography.bodyLarge)
            }
            if (moodEntry.journalEntry.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = moodEntry.journalEntry, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
