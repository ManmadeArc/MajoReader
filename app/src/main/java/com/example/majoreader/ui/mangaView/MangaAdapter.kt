package com.example.majoreader.ui.mangaView

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.majoreader.DataBase
import com.example.majoreader.MainActivity
import com.example.majoreader.MangaData
import com.example.majoreader.R
import com.example.majoreader.ui.mangaChapters.ChapterFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NullPointerException
import java.util.ArrayList
import kotlin.jvm.internal.Intrinsics

class MangaAdapter(
    private val mainActivity: MainActivity,
    private val dm: DataBase,
    private var MangaList: ArrayList<MangaData>,
    private val favPage: Boolean,

) : RecyclerView.Adapter<MangaAdapter.ListItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {

        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.manga_card, parent, false)

        return ListItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {

        var mangaData: MangaData = MangaList[position]

        mangaData = mangaData
        holder.title?.text = mangaData.title
        val parse = Uri.parse(mangaData.imageUrl)
        holder.imageView?.setImageResource(R.drawable.ic_menu_camera)
        holder.genre?.text = mangaData.genre
        holder.data = (mangaData.id)
        CoroutineScope(Dispatchers.IO).launch{
            var itsInDb = dm.itsIn(mangaData)

            withContext(Dispatchers.Main){
                if (itsInDb) {
                    holder.btn?.text = mainActivity.resources.getString(R.string.rem_fav)
                } else {
                    holder.btn?.text = mainActivity.resources.getString(R.string.fav)
                }
            }

        }

        CoroutineScope(Dispatchers.IO).launch{
            val bitmap = Glide.with(holder.imageView!!)
                .asBitmap().diskCacheStrategy(DiskCacheStrategy.DATA)
                .load(mangaData.imageUrl)
                .submit()
                .get()

            withContext(Dispatchers.Main){
                holder.imageView?.setImageBitmap(bitmap) // Update the ImageView with the loaded image
            }
        }

        holder.btn?.setOnClickListener {
            if (holder.btn!!.text == mainActivity.resources.getString(R.string.fav)) {
                holder.btn?.text = mainActivity.resources.getString(R.string.rem_fav)
                dm.insert(mangaData)
            } else {
                holder.btn?.text = mainActivity.resources.getString(R.string.fav)
                dm.delete(mangaData)
                if (favPage) {
                    this.notifyDataSetChanged()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return MangaList.size
    }


    fun getMangasList(): ArrayList<MangaData> {
        return MangaList
    }

    fun setMangasList(MangaLists: ArrayList<MangaData>) {
        MangaList = MangaLists
    }


    inner class ListItemHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var btn: Button? = null
        var data: String? = null
        var genre: TextView? = null
        var imageView: ImageView? = null
        var title: TextView? = null
        override fun onClick(view: View) {
            val chapterFragment: Fragment
            val fr: FragmentTransaction =
                mainActivity.supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            val regex = Regex("lectortmo")

            val str3 = data
            bundle.putString("id", str3)
            chapterFragment = ChapterFragment()

            chapterFragment.arguments = bundle
            fr.replace(R.id.fragmentHolder, chapterFragment).addToBackStack(null).commit()
        }

        init {
            var findViewById = view.findViewById<View>(R.id.mangaTitle)
            title = findViewById as TextView
            findViewById = view.findViewById(R.id.poster)
            imageView = findViewById as ImageView
            findViewById = view.findViewById(R.id.genre)
            genre = findViewById as TextView
            findViewById = view.findViewById(R.id.btnAFav)
            btn = findViewById as Button
            view.isClickable = true
            view.setOnClickListener(this)


        }


    }
}