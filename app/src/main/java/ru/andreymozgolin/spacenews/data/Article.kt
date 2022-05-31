package ru.andreymozgolin.spacenews.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Article(
    @PrimaryKey val id: Int,
    @NonNull val title: String,
    @NonNull val url: String,
    @NonNull val imageUrl: String,
    @NonNull val newsSite: String,
    @NonNull val summary: String,
    @NonNull val publishedAt: Date
)