package com.example.majoreader.ui.mangaChapters


import com.example.majoreader.MainActivity
import com.example.majoreader.ChapterInfo
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import kotlin.jvm.internal.Intrinsics
import android.os.Bundle
import com.example.majoreader.DataBase
import com.example.majoreader.ui.mangaVertical.VerticalReaderFragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.majoreader.R
import java.util.ArrayList

class MangaChaptersAdapter(
    val mainActivity: MainActivity,
    var mangasData: ArrayList<ChapterInfo>,
    val link: String
) : RecyclerView.Adapter<MangaChaptersAdapter.ListItemHolder>() {

    inner class ListItemHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var idx = 0
        var title:TextView = view.findViewById<View>(R.id.chapterTitle) as TextView
        lateinit var url:String
        override fun onClick(p0: View) {
            val fr = mainActivity.supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putInt(
                "index",
                mangasData.lastIndex - idx
            )
            bundle.putString(DataBase.TABLE_ROW_LINK, link)
            val fragment = VerticalReaderFragment()
            fragment.arguments = bundle
            fr.replace(R.id.fragmentHolder, fragment).addToBackStack(null).commit()
        }

        init {
            title.isClickable = false
            view.setOnClickListener(this)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        Intrinsics.checkNotNullParameter(parent, "parent")
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.manga_chapter_card, parent, false)
        return ListItemHolder( itemView)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        Intrinsics.checkNotNullParameter(holder, "holder")
        holder.title.text = mangasData[position].title
        holder.url =mangasData[position].link
        holder.idx = position
    }

    override fun getItemCount(): Int {
        return mangasData.size
    }

}