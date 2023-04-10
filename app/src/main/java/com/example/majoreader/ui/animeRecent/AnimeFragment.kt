package com.example.majoreader.ui.animeRecent

import androidx.lifecycle.lifecycleScope
import com.example.majoreader.MainActivity
import com.example.majoreader.TioAnimeScrapper
import com.example.majoreader.ChapterData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.majoreader.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList


class AnimeFragment : Fragment() {
    private lateinit var activity: MainActivity
    private lateinit var adapter: AnimeChAdapter
    private lateinit var  animeScrapper : TioAnimeScrapper
    private var data: ArrayList<ChapterData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var refresh: SwipeRefreshLayout
    private lateinit var root: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as MainActivity

        this.lifecycleScope.launch(Dispatchers.IO) {
            animeScrapper = activity.animeScrapper
            data = animeScrapper.latestEpisodes!!
            adapter = recyclerView.adapter as AnimeChAdapter
            adapter.data = data
            activity.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        refresh.isRefreshing = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.anime_recent, container, false)
        recyclerView = root.findViewById(R.id.recentRecycler) as RecyclerView
        refresh = root.findViewById(R.id.pull_refresh) as SwipeRefreshLayout
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AnimeChAdapter(requireActivity() as MainActivity, data)
        recyclerView.adapter = adapter

        refresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(activity.applicationContext, R.color.purple_500))
        refresh.setColorSchemeColors(-1)
        refresh.isRefreshing = false
        refresh.setOnRefreshListener {
            data = animeScrapper.latestEpisodes!!
            adapter.data = data
            activity.runOnUiThread {

                adapter.notifyDataSetChanged()
                refresh.isRefreshing = false
            }
        }

        return root

    }
}