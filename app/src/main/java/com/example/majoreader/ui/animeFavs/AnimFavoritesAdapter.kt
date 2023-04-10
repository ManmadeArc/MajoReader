package com.example.majoreader.ui.animeFavs

import android.net.Uri
import com.example.majoreader.MainActivity
import com.example.majoreader.SearchQuery
import com.example.majoreader.DataBase
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.majoreader.ui.animeChapters.chaptersFragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.widget.ImageView.ScaleType
import com.example.majoreader.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.jvm.internal.Intrinsics
import com.example.majoreader.ui.mangaView.MangaAdapter

import com.example.majoreader.MangaData




class AnimFavoritesAdapter(
    val mainActivity: MainActivity,
    var data: ArrayList<SearchQuery>,
    val dm: DataBase,
    var favPage: Boolean,
    val recyclerView: RecyclerView
) : RecyclerView.Adapter<AnimFavoritesAdapter.ListItemHolder>() {

    inner class ListItemHolder( view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var btn: Button = view.findViewById(R.id.btnAFav) as Button
        var image: ImageView = view.findViewById(R.id.posterAHolder) as ImageView

        var title: TextView = view.findViewById(R.id.titleAHolder) as TextView
        lateinit var url: String
        override fun onClick(p0: View) {
            val fr = mainActivity.supportFragmentManager.beginTransaction()
            val frag = chaptersFragment()
            val bundle = Bundle()
            bundle.putString("id", url)
            frag.arguments = bundle
            fr.replace(R.id.fragmentHolder, frag).addToBackStack(null).commit()
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
        holder.title.text = info.title
        holder.url= info.animId
        holder.image.scaleType = ScaleType.CENTER_CROP
        if (dm.itsIn(info)) {
            holder.btn.text = mainActivity.resources.getString(R.string.rem_fav)
        } else {
            holder.btn.text = mainActivity.resources.getString(R.string.fav)
        }
        holder.btn
            .setOnClickListener{
                if (Intrinsics.areEqual(
                       holder.btn.text,
                       mainActivity.resources.getString(R.string.fav)
                    )
                ) {
                    holder.btn.text = mainActivity.resources.getString(R.string.rem_fav)
                    dm.insert(info)
                }
                holder.btn.text = mainActivity.resources.getString(R.string.fav)
                dm.insert(info)
                if (favPage) {
                    GlobalScope.launch (Dispatchers.IO){
                        val data = ArrayList<MangaData>()
                        val info = dm.selectAll(true)
                        while (info.moveToNext()) {
                            val title: String = info.getString(1)
                            val author: String = info.getString(2)
                            val genre: String = info.getString(4)
                            val coverUrl: String = info.getString(3)
                            data.add(MangaData(title, author, genre, coverUrl))
                        }
                        info.close()
                        mainActivity.runOnUiThread {
                            (recyclerView.adapter  as MangaAdapter).setMangasList(data)
                            (recyclerView.adapter  as MangaAdapter).notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}