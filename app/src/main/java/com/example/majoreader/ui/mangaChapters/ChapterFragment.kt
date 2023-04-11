package com.example.majoreader.ui.mangaChapters
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.majoreader.*
import com.example.majoreader.ui.mangaVertical.VerticalReaderFragment
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch


class ChapterFragment : Fragment() {
    private lateinit var activity: MainActivity
    private var data = MangaFullData()
    private lateinit  var dm:DataBase
    private var genre: TextView? = null
    private var image: ImageView? = null
    private var inDataBase = false
    private var input: String? = null
    private var readBtn: Button? = null
    private var recyclerView: RecyclerView? = null
    private var synop: TextView? = null
    private var title: TextView? = null
    private lateinit var view: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity =  requireActivity() as MainActivity

        dm = DataBase(activity)
        val arguments = arguments
        input = arguments?.getString("id")!!

        inDataBase = dm.itsIn(MangaData("", "", input!!, ""))

        this.lifecycleScope.launch(Dispatchers.IO){
            var scrapper = activity.mangaScrapper
            data = scrapper.getEpisodeList(input)!!
            (recyclerView!!.adapter as MangaChaptersAdapter).mangasData = data.epiList
            activity.runOnUiThread{
                loadImage()
                (recyclerView!!.adapter as MangaChaptersAdapter).notifyDataSetChanged()
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.manga_chapters, container, false)
        recyclerView = root.findViewById(R.id.chapterList)
        image = root.findViewById(R.id.bannerA)
        synop = root.findViewById(R.id.synopsisPlaceHolder)
        genre = root.findViewById(R.id.genrePlaceHolder)
        title = root.findViewById(R.id.titlePlaceHolder)
        readBtn = root.findViewById(R.id.readBtn)
        val fr = activity.supportFragmentManager.beginTransaction()
        val bundle = Bundle()

        if (inDataBase) {
            val datas = dm[MangaData("", "", input!!, "")]
            var lastChapter = 0

            if (datas.moveToNext() == true) {
                lastChapter = datas.getInt(5)
            }

            if (lastChapter != 0) {
                readBtn?.text = getString(R.string.resumeButton)
            }

            datas.close()

            readBtn?.setOnClickListener{
                bundle.putString(
                    "url",
                    (data.epiList[data.epiList.lastIndex - lastChapter]).link
                )
                bundle.putInt("index", lastChapter)
                bundle.putString(DataBase.TABLE_ROW_LINK, input)
                val fragment = VerticalReaderFragment()
                fragment.arguments = bundle
                fr.replace(R.id.fragmentHolder, fragment).addToBackStack(null).commit()
            }
        } else {
            readBtn?.setOnClickListener{
                bundle.putString("url", data.epiList[0].link)
                bundle.putInt("index", 0)
                bundle.putString(DataBase.TABLE_ROW_LINK, input)
                val fragment = VerticalReaderFragment()
                fragment.arguments = bundle
                fr.replace(R.id.fragmentHolder, fragment).addToBackStack(null).commit()
            }
        }

        view = root.findViewById(R.id.container)
        if (data.title.isNullOrBlank()) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }


        Log.i("AAAAAA",data.imageUrl)


        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = MangaChaptersAdapter(activity, data.epiList, input!!)

        return root
    }

    fun loadImage(){
        genre?.text = "TBD"
        synop?.text = data.synopsis
        title?.text = data.title
        Glide.with(image!!)
            .load(Uri.parse(data.imageUrl))
            .into(image!!)
    }


}