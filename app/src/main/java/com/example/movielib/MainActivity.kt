
package com.example.movielib

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.room.Room
import com.example.movielib.data.DATABASE_NAME
import com.example.movielib.data.MovieLabDB
import com.example.movielib.data.models.profile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.reflect.Array
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var MovieList: MutableList<Movie>
    private lateinit var MovieTop: MutableList<TopClass>
    private var Top: String? = null
    private  lateinit var time: Handler
    private  var url: URL? = null

    private val APP = "prefs"
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences(APP, Context.MODE_PRIVATE)
        MovieList = mutableListOf()
        MovieTop = mutableListOf()

        val exec = Executors.newSingleThreadExecutor()
        exec.execute{
            getTop()
        }

        time = Handler()

        time.postDelayed({
            Log.d("Response","Дождался")
            testUpd()
        }, 3000)
        time.postDelayed({
            val reDir = Intent (this, mains::class.java )
            //Отправка данных
            val edit = prefs.edit()
            edit.putString("JSON_TOP", Top.toString())
            edit.putString("JSON_MOVIE", Gson().toJson(MovieList))
            edit.apply()

            //Переход на основную активити
            startActivity(reDir)
            finish()
        }, 12000)
    }
    fun getTop(){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://imdb8.p.rapidapi.com/title/v2/get-popular-movies-by-genre?genre=Fantasy&limit=5")
            .get()
            .addHeader("X-RapidAPI-Key", "b6b22d2376mshac86a84934d0535p19c1f7jsnf3df167ce01e")
            .addHeader("X-RapidAPI-Host", "online-movie-database.p.rapidapi.com")
            .build()
        client.newCall(request).enqueue(object : Callback{
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
                        Top = body.toString()

                        val jsonArry = JSONArray(body)
                        var x = 0
                        while (x < jsonArry.length()){
                            MovieTop.add(TopClass(jsonArry.optString(x)))

                            x++
                        }

                    }
                }
            }
        })
    }
    fun testUpd() {
    MovieTop.forEach{
        val temp = it.title.toString().split('/')
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL("https://imdb8.p.rapidapi.com/title/get-base?tconst=${temp[2]}"))
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
                    }
                    else {
                        Log.d("Response1","Запрос выполнен")

                        val body = response?.body()?.string()
                        if(body != null){
                            val book: Movie = Gson().fromJson(body, Movie::class.java)
                            if(book != null){
                                MovieList.add(book)
                            }
                        }
                    }
                }
            }
        })
    }
    }



}