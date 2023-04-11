package com.example.majoreader.ui.mangaVertical


import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import com.example.majoreader.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.util.ArrayList

class VerticalReaderFragment : Fragment() {
    private lateinit  var activity: MainActivity
    private val data = ImageData(ArrayList<String>(), "")
    private lateinit var dm: DataBase
    private var index = 0
    private var link: String? = null
    private var nxt: Button? = null
    private var prev: Button? = null
    private var recyclerView: RecyclerView? = null
    var str: String? = null
    private var string: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as MainActivity
        dm = DataBase(activity)
        string = arguments?.getString("url") ?: ""
        index = arguments?.getInt("index") ?: 0
        link = arguments?.getString(DataBase.TABLE_ROW_LINK) ?: ""
        updateValues()
        reloadData()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.chapter_vertical, container, false)
        recyclerView = root.findViewById<View>(R.id.imagesHolder) as RecyclerView
        recyclerView!!.setItemViewCacheSize(10)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.layoutManager = LinearLayoutManager(context)


        val buttons = root.findViewById<TableLayout>(R.id.chapterManager)
        val mainActivity = activity
        val adapter = VerticalChapterAdapter(mainActivity, data, buttons)
        recyclerView?.adapter = adapter
        nxt = root.findViewById<Button>(R.id.nextBtn)
        prev = root.findViewById<Button>(R.id.prevBtn)
        updateButtons()
        nxt?.setOnClickListener {
            moveToChapter(index+1)
        }
        prev?.setOnClickListener {
            moveToChapter(index - 1)
        }
        return root
    }

    fun moveToChapter(indexs: Int) {

        val lastIdx = activity.mangaScrapper.getCachedMangaData()?.epiList?.lastIndex
        string = activity.mangaScrapper.getCachedMangaData()?.epiList?.get(lastIdx!! - indexs)?.link
        index = indexs
        updateValues()
        reloadData()
        updateButtons()
        recyclerView?.scrollToPosition(0)
    }

    private fun updateValues() {
        if (dm.itsIn(MangaData("", "", link!!, ""))) {
                dm.updateChapter(MangaData("", "", link!!, ""), index)
            }
        }


    private fun reloadData() {
        GlobalScope.launch (Dispatchers.IO) {
            var scrapper = activity.mangaScrapper
            var data = scrapper.getChapterImages(string)
            activity.runOnUiThread {
                var adapter = recyclerView!!.adapter as VerticalChapterAdapter
                adapter.imageList = data!!
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun updateButtons() {
        if (index == 0) {
            if (activity.mangaScrapper.getCachedMangaData()?.epiList?.size == 1) {
                nxt?.isEnabled = false
                prev?.isEnabled = false
                return
            }
        }
        val lastIdx = activity.mangaScrapper.getCachedMangaData()?.epiList?.lastIndex ?: 0
        prev?.isEnabled = true
        nxt?.isEnabled = true

        if (index == 0) {
            prev?.isEnabled = false
        } else if (index == lastIdx) {
            nxt?.isEnabled = false
        }
    }


}