package com.example.movielib.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movielib.data.GENRE_TABLE

@Entity(tableName = GENRE_TABLE)
class genres (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(index = true)
    var genres_name: String
)