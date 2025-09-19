package com.example.eudayan.mood

import java.time.LocalDate

data class Mood(
    val name: String,
    val emoji: String
)

data class MoodEntry(
    val date: LocalDate,
    val mood: Mood,
    val journalEntry: String
)
