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
// Replaced wildcard import with specific imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun MoodLogScreen(moodViewModel: MoodViewModel = viewModel()) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val moodEntries by moodViewModel.moodEntries.collectAsState()

    var selectedMoodEntry by remember(moodEntries, currentMonth) { 
        mutableStateOf(
            moodEntries.find { 
                it.date.month == currentMonth.month && 
                it.date.year == currentMonth.year && 
                it.date.dayOfMonth == LocalDate.now().dayOfMonth 
            } ?: moodEntries.filter { 
                it.date.month == currentMonth.month && it.date.year == currentMonth.year
            }.firstOrNull()
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        MonthHeader(
            currentMonth = currentMonth,
            onPreviousMonth = { 
                currentMonth = currentMonth.minusMonths(1)
                selectedMoodEntry = moodEntries.filter { 
                    it.date.month == currentMonth.month && it.date.year == currentMonth.year
                }.firstOrNull()
            },
            onNextMonth = { 
                currentMonth = currentMonth.plusMonths(1) 
                selectedMoodEntry = moodEntries.filter { 
                    it.date.month == currentMonth.month && it.date.year == currentMonth.year
                }.firstOrNull()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CalendarView(yearMonth = currentMonth, moodEntries = moodEntries) { day ->
            selectedMoodEntry = moodEntries.find { 
                it.date.dayOfMonth == day && 
                it.date.month == currentMonth.month && 
                it.date.year == currentMonth.year 
            }
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
    val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value % 7 

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
                DayCell(day = day, moodEntry = entry, onDateSelected = onDateSelected, currentCalendarYearMonth = yearMonth) 
            }
        }
    }
}

@Composable
fun DayCell(day: Int, moodEntry: MoodEntry?, onDateSelected: (Int) -> Unit, currentCalendarYearMonth: YearMonth) {
    val cellDate = currentCalendarYearMonth.atDay(day)
    val isToday = cellDate == LocalDate.now()

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
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = day.toString(), fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .height(18.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (moodEntry != null) {
                    val emojiValue: String = moodEntry.mood.emoji 
                    Text(text = emojiValue)
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
