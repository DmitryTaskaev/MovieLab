package com.example.movielib.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movielib.data.models.favorites
import com.example.movielib.data.models.genres
import com.example.movielib.data.models.profile

@Database(entities = [favorites::class, genres::class, profile::class], version = 1)
abstract class MovieLabDB: RoomDatabase() {
    abstract fun MovieDAO(): MovieDAO

}