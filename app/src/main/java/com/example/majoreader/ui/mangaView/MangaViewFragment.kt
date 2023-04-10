package com.example.majoreader.ui.mangaView


import com.example.majoreader.MainActivity
import com.example.majoreader.MangaData
import com.example.majoreader.DataBase
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlin.jvm.internal.Ref.ObjectRef

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.majoreader.R

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Boolean
import java.lang.NullPointerException
import java.util.ArrayList
import java.util.HashMap

class MangaViewFragment : Fragment() {
    private lateinit var activity: MainActivity
    private var data: ArrayList<MangaData> = ArrayList()
    private lateinit var dm: DataBase
    private var recyclerView: RecyclerView? = null
    private var refresh: SwipeRefreshLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        activity = (requireActivity() as? MainActivity)!!
        dm = DataBase(activity)
        GlobalScope.launch(Dispatchers.IO) {
                var scrapper = activity.mangaScrapper
                data = scrapper.getLatestPopular()!!
                activity.runOnUiThread{
                    var adapter = recyclerView!!.adapter
                    adapter!!.notifyDataSetChanged()
                }
            }
        }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater: LayoutInflater = inflater
        val root: View = layoutInflater.inflate(R.layout.manga_popular, container, false)
        recyclerView = root.findViewById<View>(R.id.popularRecyler) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.layoutManager = layoutManager
        activity = requireActivity() as MainActivity
        val arrayList: ArrayList<MangaData> = data
        val dataBase = dm
        val mangaAdapter = MangaAdapter(activity,dataBase,arrayList,false )
        recyclerView!!.adapter = mangaAdapter
        refresh = root.findViewById<View>(R.id.refresh) as SwipeRefreshLayout
        refresh!!.setProgressBackgroundColorSchemeColor( ContextCompat.getColor(
                                                activity.applicationContext, R.color.purple_500))
        refresh!!.setColorSchemeColors(-1)
        refresh!!.isRefreshing = false

        refresh!!.setOnRefreshListener{
            GlobalScope.launch(Dispatchers.IO) {
                var scrapper = activity.mangaScrapper
                data = scrapper.getLatestPopular()!!
                activity.runOnUiThread{
                    var adapter = recyclerView!!.adapter
                    adapter!!.notifyDataSetChanged()
                }
            }
        }

        return root
        }
}






