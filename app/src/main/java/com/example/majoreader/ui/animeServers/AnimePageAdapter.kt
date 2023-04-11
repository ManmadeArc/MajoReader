package com.example.majoreader.ui.animeServers

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.majoreader.Server
import java.util.ArrayList

class AnimePageAdapter(var myContext: Context, var fm: FragmentActivity, var Servers: ArrayList<Server>) :
    FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return Servers.size
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString("data", Servers[position].code)
        val frag = AnimeWatchFragment()
        frag.arguments = bundle
        return frag
    }


}