package com.example.majoreader.ui.animeSearch

import android.net.Uri

import com.example.majoreader.MainActivity
import com.example.majoreader.SearchQuery
import com.example.majoreader.DataBase
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import kotlin.jvm.internal.Intrinsics
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
import java.util.ArrayList


class AnimeSearchAdapter(val mainActivity: MainActivity,var  data: ArrayList<SearchQuery>, val dm: DataBase) :
    RecyclerView.Adapter<AnimeSearchAdapter.ListItemHolder>() {

    inner class ListItemHolder( view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var backData: SearchQuery? = null
        var btn: Button? = null
        var id: String? = null
        var img: ImageView? = null
        var title: TextView? = null

        override fun onClick(p0: View) {
            val fr = mainActivity.supportFragmentManager.beginTransaction()
            val frag = chaptersFragment()
            val bundle = Bundle()
            val str = id
            bundle.putString("id", str)
            bundle.putString("idTitle", backData!!.title)
            frag.arguments = bundle
            fr.replace(R.id.fragmentHolder, frag).addToBackStack(null).commit()
        }

        init {
            img = view.findViewById(R.id.posterAHolder)
            title = view.findViewById(R.id.titleAHolder)
            btn = view.findViewById(R.id.btnAFav)
            view.isClickable = true
            view.setOnClickListener(this)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.ani_search_card, parent, false)
        return ListItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        Intrinsics.checkNotNullParameter(holder, "holder")
        val info = data[position]
        holder.backData = info
        holder.title!!.text = info.title
        holder.id = info.animId
        Glide.with(mainActivity).load(Uri.parse(info.imageUrl)).into(holder.img!!)
        holder.img!!.scaleType = ScaleType.CENTER_CROP
        if (dm.itsIn(info)) {
            holder.btn!!.text = mainActivity.resources.getString(R.string.rem_fav)
        } else {
            holder.btn!!.text = mainActivity.resources.getString(R.string.fav)
        }
        holder.btn!!.setOnClickListener{
            if (holder.btn!!.text == mainActivity.resources.getString(R.string.fav) ) {
                holder.btn!!.text = mainActivity.resources.getString(R.string.rem_fav)
                dm.insert(info)
            }
            else{
                holder.btn!!.text = mainActivity.resources.getString(R.string.fav)
                dm.delete(info)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}