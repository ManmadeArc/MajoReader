package com.example.majoreader

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.majoreader.ui.MangaFavFrag.MangaFavFrag
import com.example.majoreader.ui.animeFavs.AnimeFavorites
import com.example.majoreader.ui.animeRecent.AnimeFragment
import com.example.majoreader.ui.animeSearch.AnimeSearchFragment
import com.example.majoreader.ui.mangaView.MangaViewFragment
import com.example.majoreader.ui.search.SearchFragment
import com.google.android.material.navigation.NavigationView
import kotlin.jvm.internal.Intrinsics

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var animeScrapper: TioAnimeScrapper = TioAnimeScrapper()
    var mangaScrapper: TMOScrapper = TMOScrapper()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            findViewById(R.id.drawer_layout),
            findViewById(R.id.toolbar),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        findViewById<DrawerLayout>(R.id.drawer_layout).addDrawerListener(
            actionBarDrawerToggle
        )
        actionBarDrawerToggle.syncState()
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener(
            this
        )
        val fragment = supportFragmentManager
        fragment.beginTransaction().replace(
            R.id.fragmentHolder,
            (MangaViewFragment() as Fragment?)!!
        ).addToBackStack(null).commit()


    }

    override fun onBackPressed() {
        if (findViewById<DrawerLayout>(R.id.drawer_layout)!!.isDrawerOpen(
                GravityCompat.START
            )
        ) {
            findViewById<DrawerLayout>(R.id.drawer_layout)!!.closeDrawer(
                GravityCompat.START
            )
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Intrinsics.checkNotNullParameter(item, "item")
        return if (item.itemId != R.id.action_settings) {
            super.onOptionsItemSelected(item)
        } else true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val transaction = supportFragmentManager.beginTransaction()
        Log.i("INFO","${item.itemId}")
        when (item.itemId) {
            R.id.MangaFavs -> transaction.replace(R.id.fragmentHolder, MangaFavFrag())
            R.id.animeFavs -> transaction.replace(R.id.fragmentHolder, AnimeFavorites())
            R.id.animeRecent -> transaction.replace(R.id.fragmentHolder, AnimeFragment())
            R.id.animeSearch -> transaction.replace(R.id.fragmentHolder, AnimeSearchFragment())
            R.id.mangaSearch -> transaction.replace(R.id.fragmentHolder, SearchFragment())
            R.id.nav_popular_manga -> {
                val frag = MangaViewFragment()
                val bundle = Bundle()
                bundle.putBoolean("TMO", true)
                frag.arguments = bundle
                transaction.replace(R.id.fragmentHolder, frag)
            }
        }
        Log.i("INFO","TRANSACTION MADE")
        transaction.commit()
       findViewById<DrawerLayout>(R.id.drawer_layout)!!.closeDrawer(GravityCompat.START)
        return true
    }
}