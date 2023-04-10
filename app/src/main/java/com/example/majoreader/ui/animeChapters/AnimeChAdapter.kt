package com.example.majoreader.ui.animeChapters

import com.example.majoreader.MainActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.majoreader.ui.animeServers.AnimeTabs
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.majoreader.R
import java.util.ArrayList


class AnimeChAdapter(val mainActivity: MainActivity, var data: ArrayList<Pair<String, String>>) :
    RecyclerView.Adapter<AnimeChAdapter.ListItemHolder>() {

    inner class ListItemHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var id: String
        var text: TextView = view.findViewById<View>(R.id.chapterTitle)as TextView
        override fun onClick(p0: View) {
            val fr = mainActivity.supportFragmentManager.beginTransaction()

            val frag = AnimeTabs()
            val bundle = Bundle()
            bundle.putString("id", id)
            frag.arguments = bundle
            fr.replace(R.id.fragmentHolder, frag).addToBackStack(null).commit()
        }
        init {
            view.isClickable = true
            view.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemVIew: View =
            LayoutInflater.from(parent.context).inflate(R.layout.manga_chapter_card, parent, false)
        return ListItemHolder(itemVIew)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        var info = data[position]
        holder.text.text = "Capítulo - " +info.first
        holder.id = info.second
    }

    override fun getItemCount(): Int {
        return data.size
    }


}