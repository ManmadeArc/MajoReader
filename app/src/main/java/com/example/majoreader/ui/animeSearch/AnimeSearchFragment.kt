package com.example.majoreader.ui.animeSearch

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo

import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.majoreader.*
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

import java.util.ArrayList

class AnimeSearchFragment : Fragment() {
    private lateinit var Scrapper: TioAnimeScrapper
    private lateinit var activity: MainActivity
    private lateinit var data: ArrayList<SearchQuery>
    private lateinit var dm: DataBase
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as MainActivity
        dm = DataBase(activity)
        data = ArrayList()
        Scrapper = activity.animeScrapper
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_manga, container, false).apply {
            val recyclerView = findViewById<RecyclerView>(R.id.results)
            val text = findViewById<EditText>(R.id.searchQuery)
            val search = findViewById<ImageButton>(R.id.searchBtn)
            recyclerView?.itemAnimator = DefaultItemAnimator()
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = AnimeSearchAdapter(activity, data, dm)
            search?.setOnClickListener {
            makeSearch(text.text.toString())
            }
            text.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    makeSearch(text.text.toString())
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun makeSearch(string: String) {
        this.lifecycleScope.launch(Dispatchers.IO) {
            data = Scrapper.search(string)
            (recyclerView.adapter  as AnimeSearchAdapter).data = data
            activity.runOnUiThread {
                (recyclerView.adapter as AnimeSearchAdapter).notifyDataSetChanged()
            }
        }
    }
}