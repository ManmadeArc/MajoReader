package com.example.majoreader.ui.animeFavs

import android.database.Cursor
import android.os.Bundle
import kotlin.jvm.internal.Intrinsics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.majoreader.*
import java.util.ArrayList


class AnimeFavorites : Fragment() {
    private lateinit var Scrapper : TioAnimeScrapper
    private lateinit var data: ArrayList<SearchQuery>
    private lateinit var dm: DataBase
    private lateinit var activity:MainActivity





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = ArrayList()
        activity= requireActivity() as MainActivity
        dm = DataBase(activity)
        var info=  dm.selectAll(false)
        while (info.moveToNext()) {
            val string = info.getString(1)
            val string2 = info.getString(2)
            val string3 = info.getString(3)
            data.add(SearchQuery(string, string2, string3))
        }
        info.close()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.anime_recent, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recentRecycler)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = AnimFavoritesAdapter(activity, data, dm, true, recyclerView)

        val refresh = root.findViewById<SwipeRefreshLayout>(R.id.pull_refresh)
        refresh?.apply {
                isRefreshing = false
                isEnabled = false
        }
        return root
    }
}