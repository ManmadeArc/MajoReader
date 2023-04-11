package com.example.majoreader.ui.animeServers

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers.IO
import com.example.majoreader.MainActivity
import com.example.majoreader.TioAnimeScrapper
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.majoreader.R
import com.example.majoreader.Server
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

import java.util.ArrayList
import kotlin.jvm.internal.Intrinsics

class AnimeTabs : Fragment() {
    lateinit var activity: MainActivity
    private lateinit var  scrap : TioAnimeScrapper
    private var servers: ArrayList<Server> = ArrayList()
    private var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null
        private set
    lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as MainActivity
        scrap = activity.animeScrapper
        val arguments = arguments
        id = arguments!!.getString("id")!!




    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.anime_servers_tabs, container, false)
        tabLayout = root.findViewById<TabLayout>(R.id.serverTabs)
        viewPager = root.findViewById<ViewPager2>(R.id.iframePager)

        this.lifecycleScope.launch(IO) {
            servers = scrap.getServers(id!!)
            (viewPager!!.adapter as AnimePageAdapter).Servers = servers
            Log.i("SERVERS",servers.size.toString())
            activity.runOnUiThread {
                (viewPager!!.adapter as AnimePageAdapter).notifyDataSetChanged()
                TabLayoutMediator(tabLayout!!, viewPager!!
                ) { tab, position -> tab.text = "" + servers[position].title }
            }
        }
        Log.i("SERVERS",servers.size.toString())
        servers.forEach { server ->
            tabLayout!!.addTab(tabLayout!!.newTab().setText(server.title))
        }

        val adapter = AnimePageAdapter(requireContext(), activity, servers)
        viewPager!!.adapter = adapter

        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = servers[position].title
        }.attach()

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { viewPager!!.currentItem = it.position }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return root

    }
}