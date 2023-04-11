package com.example.majoreader.ui.mangaView


import com.example.majoreader.MainActivity
import com.example.majoreader.MangaData
import com.example.majoreader.DataBase
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.os.Bundle
import android.util.Log
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
    private val layoutManager = LinearLayoutManager(context)
    private lateinit var mangaAdapter : MangaAdapter
    private  var root: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = (requireActivity() as? MainActivity)!!
        dm = DataBase(activity)
        mangaAdapter = MangaAdapter(activity,dm,data,false )
        GlobalScope.launch(Dispatchers.IO) {
                var scrapper = activity.mangaScrapper
                data = scrapper.getLatestPopular()!!
                Log.i("INFO",data.size.toString())
                activity.runOnUiThread{
                    mangaAdapter.setMangasList(data)
                    mangaAdapter!!.notifyDataSetChanged()
                }
            }
        }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(root==null){
            val layoutInflater: LayoutInflater = inflater
            root= layoutInflater.inflate(R.layout.manga_popular, container, false)
            recyclerView = root!!.findViewById<View>(R.id.popularRecyler) as RecyclerView
            recyclerView!!.itemAnimator = DefaultItemAnimator()
            if(recyclerView!!.layoutManager==null){
                recyclerView!!.layoutManager = layoutManager
            }
            activity = requireActivity() as MainActivity
            recyclerView!!.adapter = mangaAdapter
            refresh = root!!.findViewById<View>(R.id.refresh) as SwipeRefreshLayout
            refresh!!.setProgressBackgroundColorSchemeColor( ContextCompat.getColor(
                activity.applicationContext, R.color.purple_500))
            refresh!!.setColorSchemeColors(-1)


            refresh!!.setOnRefreshListener{
                GlobalScope.launch(Dispatchers.IO) {
                    var scrapper = activity.mangaScrapper
                    data = scrapper.getLatestPopular()!!
                    activity.runOnUiThread{
                        mangaAdapter!!.notifyDataSetChanged()
                        refresh!!.isRefreshing = false
                    }
                }
            }
        }


        return root
        }
}






