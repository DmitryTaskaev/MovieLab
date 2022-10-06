package com.example.movielib.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movielib.data.PROFILE_TABLE

@Entity(tableName = PROFILE_TABLE)
data class profile(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var genresId: Int

)