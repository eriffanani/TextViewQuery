package com.erif.textviewquery.ui

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.erif.textviewquery.R
import com.erif.textviewquery.databinding.ActivityAnotherExampleBinding
import com.erif.textviewquery.model.ModelItemMain
import com.erif.textviewquery.model.ModelItemMainFilter
import com.erif.textviewquery.ui.adapter.main.AdapterMain
import com.erif.textviewquery.ui.viewmodel.MainViewModel

class AnotherExample : AppCompatActivity() {

    private val adapterMain = AdapterMain()
    private val viewModel: MainViewModel by viewModels()

    private var menuItem: MenuItem? = null
    private var searchView: SearchView? = null

    private var defaultList: MutableList<ModelItemMainFilter> = arrayListOf()
    private var searchList: MutableList<ModelItemMain> = arrayListOf()

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAnotherExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.actAnotherToolbar
        setSupportActionBar(toolbar)

        recyclerView = binding.actAnotherRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AnotherExample)
            adapter = adapterMain
        }
        clearRecyclerAnim(recyclerView)
        viewModel.mainData().forEach {
            defaultList.add(
                ModelItemMainFilter(it.id, it.image, it.name, it.subtitle, it.query)
            )
        }
        defaultList.forEach {
            searchList.add(ModelItemMain(it.id, it.image, it.name, it.subtitle, it.query))
        }
        adapterMain.setList(searchList)

        /*viewModel.delay(1000L) {
            val pos = 0
            searchList[pos].query = "T"
            adapterMain.notifyItemChanged(pos)
        }*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        menuItem = menu?.findItem(R.id.menu_search)
        searchView = menuItem?.actionView as SearchView?
        searchView?.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        searchView?.setOnQueryTextListener(searchListener)
        return super.onCreateOptionsMenu(menu)
    }

    private val searchListener = object : OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            closeSearch()
            return false
        }

        override fun onQueryTextChange(query: String?): Boolean {
            val mQuery = query ?: ""
            if (mQuery.isNotBlank() && mQuery.isNotEmpty()) {
                search(query)
            } else {
                searchList.clear()
                defaultList.forEach {
                    searchList.add(ModelItemMain(it.id, it.image, it.name, it.subtitle, it.query))
                }
                adapterMain.setList(searchList)
            }
            val manager = recyclerView.layoutManager as LinearLayoutManager
            manager.scrollToPositionWithOffset(0, 0)
            return false
        }
    }

    private fun search(mQuery: String?) {
        val query = mQuery ?: ""
        val emptyQuery = TextUtils.isEmpty(query)
        val blankQuery = query.isBlank()
        if (!emptyQuery && !blankQuery) {
            val newList = arrayListOf<ModelItemMain>()
            defaultList.forEach {
                newList.add(ModelItemMain(it.id, it.image, it.name, it.subtitle, it.query))
            }
            val results = viewModel.selectedListName(query, newList)
            results.forEachIndexed { idx, res ->
                res.query = query
                val found = searchList.find { it.id == res.id }
                val notFound = found == null
                if (notFound) {
                    searchList.add(idx, res)
                    adapterMain.notifyItemInserted(searchList.size)
                } else {
                    found?.let {
                        val index = searchList.indexOf(it)
                        Log.e("mantul", "${res.name} from $index to $idx")
                        if (index >= 0) {
                            searchList[index].query = query
                            adapterMain.notifyItemChanged(index)
                            if (index != idx) {
                                searchList.removeAt(index)
                                adapterMain.notifyItemRemoved(index)
                                searchList.add(idx, res)
                                adapterMain.notifyItemInserted(idx)
                            }
                        }
                    }
                }
            }
            Log.e("mantul", "Last Index:  ${searchList[1].query}")
        } else if (emptyQuery) {
            searchList.clear()
        }
    }

    private fun closeSearch() {
        if (searchView?.isIconified == false)
            searchView?.isIconified = true
        menuItem?.collapseActionView()
    }

    private fun clearRecyclerAnim(recyclerView: RecyclerView) {
        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        (recyclerView.itemAnimator as DefaultItemAnimator).endAnimations()
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        (recyclerView.itemAnimator)?.changeDuration = 0
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}