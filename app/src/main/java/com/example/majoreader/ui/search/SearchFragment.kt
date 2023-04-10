package com.example.majoreader.ui.search
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.jvm.internal.Ref.ObjectRef
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.majoreader.*
import com.example.majoreader.ui.mangaView.MangaAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList


class SearchFragment : Fragment() {
    private var activity: MainActivity? = null
    var data: ArrayList<MangaData>? = null
    private var dm: DataBase? = null
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requireActivity = requireActivity()
        if (requireActivity != null) {
            val mainActivity = requireActivity as MainActivity
            activity = mainActivity
            val str = "activity"
            val mainActivity2 = activity
            data = ArrayList<MangaData>()
            val requireActivity2 = requireActivity()
            dm = DataBase(requireActivity2)
            return
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.search_manga, container, false)
        recyclerView = root.findViewById(R.id.results) as RecyclerView
        val text  = root.findViewById(R.id.searchQuery) as EditText
        val search = root.findViewById<ImageButton>(R.id.searchBtn)
        text.setOnClickListener { // Call the function defined in the fragment
            makeSearch(text.text.toString())
        }
        search.setOnClickListener{
            makeSearch(text.text.toString())
        }
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = MangaAdapter(requireActivity() as MainActivity, dm!! ,data!!,false )

        return root
    }

    private fun makeSearch(string: String) {
        GlobalScope.launch {
            val data = activity?.mangaScrapper?.search(string,0)
            activity?.runOnUiThread  {
                val adapter = recyclerView?.adapter as? MangaAdapter
                adapter?.setMangasList(data!!)
                adapter?.notifyDataSetChanged()
            }
        }
    }


}
