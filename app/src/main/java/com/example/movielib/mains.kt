package com.example.movielib

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.movielib.data.CompilationAdapter
import com.example.movielib.data.DATABASE_NAME
import com.example.movielib.data.MovieLabDB
import com.example.movielib.data.handler
import com.example.movielib.data.models.favorites
import com.example.movielib.data.models.genres
import com.example.movielib.data.models.profile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.util.concurrent.Executors

class mains : AppCompatActivity() {
    private val APP = "prefs"
    private lateinit var prefs: SharedPreferences
    private lateinit var MovieTop: MutableList<TopClass>
    private lateinit var MovieList: MutableList<Movie>
    private lateinit var FavList: MutableList<favorites>
    private lateinit var GenresList: MutableList<genresTemp>
    private lateinit var profile: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mains)
        //База
        var db: MovieLabDB = Room.databaseBuilder(this, MovieLabDB::class.java, DATABASE_NAME).build()
        val MovieDao = db.MovieDAO()
        val exec = Executors.newSingleThreadExecutor()
        exec.execute{
            if(MovieDao.getProfileCount() == 0){
                MovieDao.addProfile(profile(0,1))
            }
            if(MovieDao.getCountGenres() == 0){
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://online-movie-database.p.rapidapi.com/title/get-genres?tconst=tt0944947")
                    .get()
                    .addHeader("X-RapidAPI-Key", "b6b22d2376mshac86a84934d0535p19c1f7jsnf3df167ce01e")
                    .addHeader("X-RapidAPI-Host", "online-movie-database.p.rapidapi.com")
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }
                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if(!response.isSuccessful){
                                Log.d("Response","Неудачный запрос")
                            }
                            else {
                                val body = response?.body()?.string()
                                val jsonArry = JSONArray(body)
                                var x = 0
                                while (x < jsonArry.length()){
                                        MovieDao.addGenres(genres(0,jsonArry.optString(x)))
                                    x++
                                }

                            }
                        }
                    }
                })
            }
        }
        /*
        if(MovieDao.getProfileCount() == 0){
            MovieDao.addProfile(profile(0,1))
        }

         */

        profile = findViewById(R.id.profileBtn)
        profile.setOnClickListener{
            val intent = Intent(this, com.example.movielib.profile::class.java)
            startActivity(intent)
        }
        //Листы и prefs
        updateTopList()
        updateCompilationRV()
        updateFavorites()

    }

    override fun onResume() {
        super.onResume()
        updateCompilationRV()
        updateFavorites()
    }
    fun updateFavorites(){
        FavList.clear()
        var db: MovieLabDB = Room.databaseBuilder(this, MovieLabDB::class.java, DATABASE_NAME).build()
        val MovieDao = db.MovieDAO()
        val Fav = MovieDao.getAllFavorites()
        Fav.observe(this, androidx.lifecycle.Observer {
            it.forEach{
                FavList.add(favorites(it.uid,it.genres,it.nameMovie,it.imgMobie,it.yearMovie))
            }
        })
    }
    fun updateTopList() {
        prefs = getSharedPreferences(APP, Context.MODE_PRIVATE)
        MovieList = mutableListOf()
        MovieTop = mutableListOf()
        FavList = mutableListOf()
        //Парсинг данных
        if(prefs.contains("JSON_TOP")){
            val json:String? = prefs.getString("JSON_TOP", "")
            val jsonArry = JSONArray(json)
            var x = 0
            while (x < jsonArry.length()){
                MovieTop.add(TopClass(jsonArry.optString(x)))
                x++
            }
        }
        else { Log.d("Response","Prefs clear(Movie Top)") }
    }

    fun updateCompilationRV(){
        MovieList.clear()
        if(prefs.contains("JSON_MOVIE")){
            val json:String? = prefs.getString("JSON_MOVIE", "")
            MovieList = Gson().fromJson<MutableList<Movie>>(json, object: TypeToken<MutableList<Movie>>() {}.type)
                .toMutableList()
        }
        else { Log.d("Response","Prefs clear(Movie List)") }
        val rv = findViewById<RecyclerView>(R.id.compilationRV)
        val adapter = CompilationAdapter(this, MovieList)
        val rvListener = object : CompilationAdapter.ItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(this@mains, movieinfo::class.java)
                intent.putExtra("index", position)
                var temp: Boolean? = null
                FavList.forEach {
                    if(it.nameMovie == MovieList[position].title){
                        temp = true
                        intent.putExtra("uuid", it.uid.toString())
                    }
                    else {
                        temp = false
                    }
                }
                intent.putExtra("fav", temp)
                startActivity(intent)
            }
        }
        adapter.setClickListener(rvListener)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = adapter
    }
}