package com.example.majoreader.ui.MangaFavFrag


import com.example.majoreader.MangaData
import com.example.majoreader.DataBase
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log

import kotlin.jvm.internal.Intrinsics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.majoreader.MainActivity
import com.example.majoreader.ui.mangaView.MangaAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.majoreader.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList



class MangaFavFrag : Fragment() {
    private var data: ArrayList<MangaData> = ArrayList()
    private lateinit var dm: DataBase
    private lateinit var activity: MainActivity
    lateinit var recyclerView :RecyclerView
    lateinit var adapter: MangaAdapter
    lateinit var layoutManager:LinearLayoutManager
    var root:View? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as MainActivity
        dm = DataBase(context)
        data = ArrayList<MangaData>()
        layoutManager = LinearLayoutManager(context)
        (layoutManager).initialPrefetchItemCount = 10

        adapter = MangaAdapter(activity,dm,ArrayList<MangaData>(),true)
        this.lifecycleScope.launch(Dispatchers.IO) {
            var info = dm.selectAll(true)
            Log.i("SIZE",info.count.toString())

            while (info.moveToNext()) {
                val string = info.getString(1)
                val string2 = info.getString(2)
                val string3 = info.getString(4)
                val string4 = info.getString(3)
                data.add(MangaData(string, string2, string3, string4))
            }

            info.close()
            lifecycleScope.launch{
                adapter.setMangasList(data)
                adapter!!.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(root==null){
            root = inflater.inflate(R.layout.manga_popular, container, false)
            recyclerView = root!!.findViewById(R.id.popularRecyler)
            recyclerView.itemAnimator = DefaultItemAnimator()
            if(recyclerView.layoutManager==null){
                recyclerView.layoutManager = layoutManager
            }

            recyclerView.adapter = adapter
            var refresh: SwipeRefreshLayout? = root!!.findViewById(R.id.refresh)
            refresh = refresh

            refresh!!.isRefreshing = false
            refresh.isEnabled = false
        }

        return root

    }
}