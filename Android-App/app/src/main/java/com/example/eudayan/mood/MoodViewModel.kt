package com.example.eudayan.mood

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

// Placeholder for where you'd get your actual data from (e.g., a repository)
object MoodRepository {
    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(
        // Initial hardcoded data - replace with actual data loading
        listOf(
            MoodEntry(LocalDate.now().withDayOfMonth(12), Mood("Happy", "😀"), "Had a great day at college!"),
            MoodEntry(LocalDate.now().withDayOfMonth(13), Mood("Happy", "😀"), ""),
            MoodEntry(LocalDate.now().withDayOfMonth(15), Mood("Sad", "😞"), "Feeling a bit down today."),
            MoodEntry(LocalDate.now().withDayOfMonth(17), Mood("Happy", "😀"), "Passed my exam!"),
            MoodEntry(LocalDate.now().withDayOfMonth(5), Mood("Calm", "😌"), "Peaceful morning meditation."),
            MoodEntry(LocalDate.now().withDayOfMonth(8), Mood("Anxious", "😟"), "Worried about upcoming presentation."),
            MoodEntry(LocalDate.now().minusMonths(1).withDayOfMonth(28), Mood("Happy", "😀"), "Weekend getaway was fun!"),
            MoodEntry(LocalDate.now().withDayOfMonth(2), Mood("Sad", "😞"), "Missing my family.")
        )
    )
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries

    fun addMoodEntry(entry: MoodEntry) {
        val currentEntries = _moodEntries.value.toMutableList()
        // Remove if an entry for the same date exists, then add the new one
        currentEntries.removeAll { it.date == entry.date }
        currentEntries.add(entry)
        // Sort by date for consistency, optional
        _moodEntries.value = currentEntries.sortedBy { it.date }
    }
}

class MoodViewModel : ViewModel() {
    // In a real app, you would inject a repository here
    private val repository = MoodRepository // Using the placeholder repository

    val moodEntries: StateFlow<List<MoodEntry>> = repository.moodEntries

    // Example function to add a mood entry - you'll call this from where you submit moods
    fun addMoodEntry(date: LocalDate, mood: Mood, journalEntry: String) {
        val newEntry = MoodEntry(date, mood, journalEntry)
        repository.addMoodEntry(newEntry)
    }
}
