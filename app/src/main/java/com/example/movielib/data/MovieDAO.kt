package com.example.movielib.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movielib.data.models.favorites
import com.example.movielib.data.models.genres
import com.example.movielib.data.models.profile

@Dao
interface MovieDAO{
    //Profile

    @Query("SELECT * FROM $PROFILE_TABLE")
    fun getProfile(): LiveData<MutableList<profile>>

    @Query("SELECT * FROM $PROFILE_TABLE WHERE genresId ")
    fun getGenres(): Int

    @Query("SELECT COUNT(*) FROM $PROFILE_TABLE")
    fun getProfileCount():Int

    @Insert
    fun addProfile(profile: profile)

    @Update
    fun updateProfile(profile: profile)
    //Favorites

    @Query("SELECT * FROM $FAVORITE_TABLE")
    fun getAllFavorites(): LiveData<MutableList<favorites>>

    @Query("SELECT * FROM $FAVORITE_TABLE WHERE nameMovie=:name")
    fun getFavForName(name: String): LiveData<MutableList<favorites>>

    @Insert
    fun addFavorites(favorites: favorites)

    @Delete
    fun delFavorites(favorites: favorites)


    //Genres

    @Query("SELECT * FROM $GENRE_TABLE")
    fun getAllGenres(): LiveData<MutableList<genres>>

    @Query("SELECT COUNT(*) FROM $GENRE_TABLE")
    fun getCountGenres(): Int

    @Insert
    fun addGenres(genres: genres)
}