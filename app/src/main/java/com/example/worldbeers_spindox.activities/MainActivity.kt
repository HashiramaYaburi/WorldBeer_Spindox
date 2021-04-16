package com.example.worldbeers_spindox.activities

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worldbeers_spindox.adapters.Adapter
import com.example.worldbeers_spindox.models.Beer
import com.example.worldbeers_spindox.R
import com.example.worldbeers_spindox.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //list of values
    var arrayList = ArrayList<Beer>()
    //list of displayed values depending on scroll and search
    var displayList = ArrayList<Beer>()
    var isLoading = false
    var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchJson(index)
        initAdapter();
        initScrollListener();
    }

    //Search parameters basing on title and description
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val menuItem = menu!!.findItem(R.id.search)

        if(menuItem != null) {
            val searchView = menuItem.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()) {
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        arrayList.forEach {
                            if (it.name.toLowerCase(Locale.getDefault()).contains(search) || it.description.toLowerCase(Locale.getDefault()).contains(search)) {
                                displayList.add(it)
                            }
                        }
                        binding.rvRecyclerView.adapter!!.notifyDataSetChanged()
                    } else {
                        displayList.clear()
                        displayList.addAll(arrayList)
                        binding.rvRecyclerView.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }
            })

        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
    //function which parse json from constructed url. The two lists are updated and adapter is notified to reload new modules
    fun fetchJson(page_index: Int){
            val url ="https://api.punkapi.com/v2/beers?page=$page_index&per_page=25"
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient();
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response?.body?.string()
                    println(body)
                    val gson = GsonBuilder().create()

                    val homeFeed = gson.fromJson(body, Array<Beer>::class.java).toList()

                    displayList.addAll(homeFeed)
                    arrayList.addAll(homeFeed)

                    runOnUiThread {
                        binding.rvRecyclerView.adapter!!.notifyDataSetChanged()
                    }

                }
            })
    }

    private fun initAdapter(){
        val myAdapter = Adapter(displayList, this)
        binding.rvRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.rvRecyclerView.adapter = myAdapter
    }

    private fun initScrollListener(){
        binding.rvRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == displayList.size - 1) {
                        //bottom of list!
                        loadMore()
                        index += 1
                        isLoading = true
                    }
                }
            }
        })
    }
    //get size of list, update it and load new data from another page_index
    private fun loadMore() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val scrollPosition: Int = displayList.size
            var currentSize = scrollPosition
            val nextLimit = currentSize + 25
            if (currentSize - 1 < nextLimit) {
                fetchJson(index)
            }
            isLoading = false
        }, 2000)
    }
}

