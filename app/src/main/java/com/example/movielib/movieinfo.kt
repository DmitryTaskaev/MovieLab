package com.example.movielib

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room
import com.example.movielib.data.DATABASE_NAME
import com.example.movielib.data.MovieLabDB
import com.example.movielib.data.models.favorites
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.concurrent.Executors

private lateinit var prefs: SharedPreferences
private lateinit var MovieList: MutableList<Movie>
private lateinit var FavList: MutableList<favorites>
private val APP = "prefs"

class movieinfo : AppCompatActivity() {

    private lateinit var nameMovie: TextView
    private lateinit var yearMovie: TextView
    private lateinit var genresMovie: TextView
    private lateinit var imageMovie: ImageView
    private lateinit var backbtn: ImageButton
    private lateinit var favbtn: ImageButton

    val handler = Handler(Looper.getMainLooper())
    var image: Bitmap? = null
    var favorit: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movieinfo)
        prefs = getSharedPreferences(APP, Context.MODE_PRIVATE)
        MovieList = mutableListOf()
        FavList = mutableListOf()

        nameMovie = findViewById(R.id.movieName)
        yearMovie = findViewById(R.id.yearMovie)
        genresMovie = findViewById(R.id.genresMovie)
        imageMovie = findViewById(R.id.favImg)
        backbtn = findViewById(R.id.backButton)
        favbtn = findViewById(R.id.favButton)

        val json:String? = prefs.getString("JSON_MOVIE", "")
        var index = intent.getIntExtra("index",-1)
        var fav = intent.getBooleanExtra("fav",false)
        if(fav){
            favbtn.setImageResource(R.drawable.starfull)
            favorit = true
        }
        Log.d("Response","Info: ${index}")
        MovieList = Gson().fromJson<MutableList<Movie>>(json, object: TypeToken<MutableList<Movie>>() {}.type)
            .toMutableList()
        nameMovie.text = MovieList[index!!].title.toString()
        yearMovie.text = MovieList[index!!].year.toString()
        genresMovie.text = MovieList[index].titleType.toString()

        val executor = Executors.newSingleThreadExecutor()
        executor.execute(){
            val imageURL = MovieList[index!!].image?.url
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
                handler.post {
                    imageMovie.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        backbtn.setOnClickListener {
            super.onBackPressed()
        }
        favbtn.setOnClickListener {
            if(favorit == true){
                var db: MovieLabDB = Room.databaseBuilder(this, MovieLabDB::class.java, DATABASE_NAME).build()
                val MovieDao = db.MovieDAO()
                val exec = Executors.newSingleThreadExecutor()
                exec.execute{
                    MovieDao.delFavorites(favorites(UUID.fromString(intent.getStringExtra("uuid")),MovieList[index].titleType.toString(), MovieList[index!!].title.toString(), MovieList[index!!].image?.url.toString(), MovieList[index!!].year.toString()))
                }
                favbtn.setImageResource(R.drawable.starstroke)
                favorit = false
            }
            else {
                var db: MovieLabDB = Room.databaseBuilder(this, MovieLabDB::class.java, DATABASE_NAME).build()
                val MovieDao = db.MovieDAO()
                val exec = Executors.newSingleThreadExecutor()
                exec.execute{
                    MovieDao.addFavorites(favorites(UUID.randomUUID(), MovieList[index].titleType.toString(), MovieList[index!!].title.toString(), MovieList[index!!].image?.url.toString(), MovieList[index!!].year.toString()))
                }
                favbtn.setImageResource(R.drawable.starfull)
                favorit = true
            }
        }

    }
}