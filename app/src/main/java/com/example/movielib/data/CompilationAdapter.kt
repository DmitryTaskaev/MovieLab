package com.example.movielib.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movielib.Movie
import com.example.movielib.R
import okhttp3.*
import java.util.concurrent.Executors

val handler = Handler(Looper.getMainLooper())
var image: Bitmap? = null

class CompilationAdapter(context: Context, val data: MutableList<Movie>) : RecyclerView.Adapter<CompilationAdapter.CompilatioViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private var iClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompilatioViewHolder {
        val view: View = layoutInflater.inflate(R.layout.moviecard,parent, false)
        return CompilatioViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompilatioViewHolder, position: Int) {
        val item = data[position]
        holder.TitleMovie.text = item?.title
        val executor = Executors.newSingleThreadExecutor()
        executor.execute(){
            val imageURL = item?.image?.url
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
                handler.post {
                    holder.imageView.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun getItemCount(): Int = data.size

    fun setClickListener(itemClickListener: ItemClickListener?){
        iClickListener = itemClickListener
    }

    interface ItemClickListener{
        fun onItemClick(view: View?, position: Int)
    }

    inner class CompilatioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var TitleMovie : TextView = itemView.findViewById(R.id.titleMovie)
        var imageView : ImageView = itemView.findViewById(R.id.favImg)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            iClickListener?.onItemClick(view, adapterPosition)
        }

    }
}