package com.example.majoreader.ui.animeChapters

import android.net.Uri

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers.IO
import com.example.majoreader.AnimeEpisodes
import com.example.majoreader.TioAnimeScrapper
import com.example.majoreader.MainActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.LinearLayout
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.majoreader.R

import kotlinx.coroutines.async


class chaptersFragment : Fragment() {
    private lateinit var data : AnimeEpisodes
    private lateinit var scrap: TioAnimeScrapper
    private lateinit var activity: MainActivity
    private lateinit var image: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var syn: TextView
    private lateinit var title: TextView
    private lateinit var view: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var string= arguments?.getString("id")
        activity = requireActivity() as MainActivity
        data = AnimeEpisodes()
        scrap = activity.animeScrapper
        lifecycleScope.async(IO) {
            data  = scrap.getAnimeEpiList(string)
            var adapter = (recyclerView.adapter as AnimeChAdapter)
            adapter.data = data.epiList

            activity.runOnUiThread {
                adapter.notifyDataSetChanged()
                view.visibility = View.VISIBLE
                title.text = data.title
                syn.text = data.synopsis
                Log.i("IMAGE",data.image.toString())
                Glide.with(activity).load(Uri.parse(data.image)).into(image)
                image.scaleType = ScaleType.CENTER_CROP
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.anime_data_lay, container, false)

        root.findViewById<ImageView>(R.id.bannerA)?.let { imageView ->
            image = imageView
            title = root.findViewById<TextView>(R.id.titlePlaceHolder)?.apply { text = data.title }!!
            syn = root.findViewById<TextView>(R.id.synopsisPlaceHolder)?.apply { text = data.synopsis }!!
            recyclerView = root.findViewById<RecyclerView>(R.id.chapterList)?.apply {
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                adapter = AnimeChAdapter(requireActivity() as MainActivity, data.epiList)
            }!!
            view = root.findViewById<LinearLayout>(R.id.container)?.apply { visibility = View.VISIBLE }!!

            val imageUrl = data.image
            Glide.with(this).load(Uri.parse(imageUrl)).into(image)
            image.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        return root
    }


}