package com.example.movielib.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movielib.data.FAVORITE_TABLE
import java.util.*

@Entity(tableName = FAVORITE_TABLE)
class favorites (
    @PrimaryKey(autoGenerate = false)
    val uid: UUID,
    var genres: String,
    var nameMovie: String,
    var imgMobie: String,
    var yearMovie: String
)