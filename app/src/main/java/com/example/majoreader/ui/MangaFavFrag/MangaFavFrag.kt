package com.example.majoreader.ui.MangaFavFrag


import com.example.majoreader.MangaData
import com.example.majoreader.DataBase
import android.os.Bundle

import kotlin.jvm.internal.Intrinsics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.majoreader.MainActivity
import com.example.majoreader.ui.mangaView.MangaAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.majoreader.R
import java.util.ArrayList



class MangaFavFrag : Fragment() {
    private var data: ArrayList<MangaData> = ArrayList()
    private lateinit var dm: DataBase
    private lateinit var activity: MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        activity = requireActivity() as MainActivity
        dm = DataBase(activity)
        super.onCreate(savedInstanceState)
        data = ArrayList<MangaData>()


        var info = dm.selectAll(true)
        while (info.moveToNext()) {

            val string = info.getString(1)
            val string2 = info.getString(2)
            val string3 = info.getString(4)
            val string4 = info.getString(3)
            data.add(MangaData(string, string2, string3, string4))
        }
        info.close()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Intrinsics.checkNotNullParameter(inflater, "inflater")
        val root: View = inflater.inflate(R.layout.manga_popular, container, false)
        var recyclerView: RecyclerView? = root.findViewById(R.id.popularRecyler)
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MangaAdapter(activity,dm,data,true)
        var refresh: SwipeRefreshLayout? = root.findViewById(R.id.refresh)
        refresh = refresh
        refresh!!.isRefreshing = false
        refresh.isEnabled = false
        return root

    }
}