package com.example.movielib

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.movielib.data.CompilationAdapter
import com.example.movielib.data.DATABASE_NAME
import com.example.movielib.data.MovieLabDB
import com.example.movielib.data.models.favorites

class profile : AppCompatActivity() {
    private lateinit var Adventure: Button
    private lateinit var Action: Button
    private lateinit var Fantasy: Button
    private lateinit var Dramma: Button
    private lateinit var crlear: Button
    private lateinit var FavList: MutableList<favorites>

    var informs: Int? = 0

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        FavList = mutableListOf()
        updateFavorites()
        Adventure = findViewById(R.id.btnAdventure)
        Dramma = findViewById(R.id.btnDramma)
        Fantasy = findViewById(R.id.btnFantasy)
        Action = findViewById(R.id.btnAction)
        crlear = findViewById(R.id.clearFavoritesList)

        Adventure.setOnClickListener{ //1 id
            informs = 1
            Adventure.setBackgroundColor(getResources().getColor(R.color.btnprofileChecked))
            Dramma.setBackgroundColor(getResources().getColor(R.color.btnprofile))
            Fantasy.setBackgroundColor(getResources().getColor(R.color.btnprofile))
            Action.setBackgroundColor(getResources().getColor(R.color.btnprofile))

        }
        Dramma.setOnClickListener{ //2 id
            informs = 1
            Adventure.setBackgroundColor(getResources().getColor(R.color.btnprofile))
            Dramma.setBackgroundColor(getResources().getColor(R.color.btnprofileChecked))
            Fantasy.setBackgroundColor(getResources().getColor(R.color.btnprofile))
            Action.setBackgroundColor(getResources().getColor(R.color.btnprofile))


        }
        Fantasy.setOnClickListener{ //3 id
            informs = 1
            Adventure.setBackgroundColor(getResources().getColor(R.color.btnprofile))
            Dramma.setBackgroundColor(getResources().getColor(R.color.btnprofile))
            Fantasy.setBackgroundColor(getResources().getColor(R.color.btnprofileChecked))
            Action.setBackgroundColor(getResources().getColor(R.color.btnprofile))

        }
        Action.setOnClickListener { //0 id
            informs = 1
            Adventure.setBackgroundColor(getResources().getColor(R.color.btnprofile))
            Dramma.setBackgroundColor(getResources().getColor(R.color.btnprofile))
            Fantasy.setBackgroundColor(getResources().getColor(R.color.btnprofile))
            Action.setBackgroundColor(getResources().getColor(R.color.btnprofileChecked))

        }
        crlear.setOnClickListener{
            FavList.clear()
            updateResysle()
        }
    }
    fun updateFavorites(){
        FavList.clear()
        var db: MovieLabDB = Room.databaseBuilder(this, MovieLabDB::class.java, DATABASE_NAME).build()
        val MovieDao = db.MovieDAO()
        val Fav = MovieDao.getAllFavorites()
        Fav.observe(this, androidx.lifecycle.Observer {
            it.forEach{
                FavList.add(favorites(it.uid,it.genres,it.nameMovie,it.imgMobie,it.yearMovie))
                updateResysle()
            }
        })
    }
    fun updateResysle(){
        val rv = findViewById<RecyclerView>(R.id.favRV)
        val adapter = FavoritesAdapter(this, FavList)
        val rvListener = object : FavoritesAdapter.ItemClickListener{
            override fun onItemClick(view: View?, position: Int) {

            }
        }
        adapter.setClickListener(rvListener)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }
}