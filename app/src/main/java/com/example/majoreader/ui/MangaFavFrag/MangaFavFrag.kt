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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MangaAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 0
    private var itemsPerPage = 20
    private var root:View? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as MainActivity
        dm = DataBase(context)
        layoutManager = LinearLayoutManager(context)
        (layoutManager).initialPrefetchItemCount = 10

        adapter = MangaAdapter(activity, dm, ArrayList<MangaData>(), true)
        loadMangaData(currentPage)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(root!=null) return  root
         root = inflater.inflate(R.layout.manga_popular, container, false)
        recyclerView = root!!.findViewById(R.id.popularRecyler)
        recyclerView.itemAnimator = DefaultItemAnimator()
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = layoutManager
        }

        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        loadMangaData(currentPage + 1)
                    }
                }
            }
        })

        val refresh: SwipeRefreshLayout? = root!!.findViewById(R.id.refresh)
        refresh?.isEnabled = false

        return root
    }

    private fun loadMangaData(page: Int) {
        isLoading = true
        this.lifecycleScope.launch(Dispatchers.IO) {
            val info = dm.selectAll(true, itemsPerPage,page)
            val newData = ArrayList<MangaData>()

            while (info.moveToNext()) {
                val string = info.getString(1)
                val string2 = info.getString(2)
                val string3 = info.getString(4)
                val string4 = info.getString(3)
                newData.add(MangaData(string, string2, string3, string4))
            }

            info.close()
            lifecycleScope.launch(Dispatchers.Main) {
                if (page == 0) {
                    data.clear()
                }

                adapter.addMangasList(newData)
                isLoading = false

                if (newData.isEmpty()) {
                    isLastPage = true
                } else {
                    currentPage = page
                }
            }
        }
    }
}