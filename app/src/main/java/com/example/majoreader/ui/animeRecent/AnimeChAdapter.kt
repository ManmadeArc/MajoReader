package com.example.majoreader.ui.animeRecent

import android.net.Uri
import com.example.majoreader.MainActivity
import com.example.majoreader.ChapterData
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.os.Bundle
import com.example.majoreader.ui.animeServers.AnimeTabs
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.widget.ImageView.ScaleType
import com.example.majoreader.R
import java.util.ArrayList

class AnimeChAdapter(val mainActivity: MainActivity, var data: ArrayList<ChapterData>) :
    RecyclerView.Adapter<AnimeChAdapter.ListItemHolder>() {


    inner class ListItemHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var image: ImageView = view.findViewById<View>(R.id.posterAHolder) as ImageView
        var title: TextView = view.findViewById(R.id.titleAHolder) as TextView
        var url: String? = null
        override fun onClick(p0: View) {
            val fr = mainActivity.supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putString("id", url)
            val fragment = AnimeTabs()
            fragment.arguments = bundle
            fr.replace(R.id.fragmentHolder, fragment).addToBackStack(null).commit()
        }

        init {
            view.isClickable = true
            view.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.anime_chapter, parent, false)
        return ListItemHolder( itemView)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        var info = data[position]
        Glide.with(holder.image.context).load(Uri.parse(info.imageUrl)).into(holder.image)
        holder.title.text = info.chapterTitle
        holder.url = info.chapterId
        holder.image.scaleType = ScaleType.CENTER_CROP
    }

    override fun getItemCount(): Int {
        return data.size
    }

}