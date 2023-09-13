package com.erif.textviewquery.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
import com.erif.textviewquery.usecases.MainListItemListener
import java.util.Collections

class AnotherExample : AppCompatActivity(), MainListItemListener {

    private val adapterMain = AdapterMain(this)
    private val viewModel: MainViewModel by viewModels()

    private var menuItem: MenuItem? = null
    private var searchView: SearchView? = null

    private var defaultList: MutableList<ModelItemMainFilter> = arrayListOf()
    private var searchList: MutableList<ModelItemMain> = arrayListOf()

    private lateinit var recyclerView: RecyclerView

    private var results: MutableList<ModelItemMain> = arrayListOf()

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
        applyDefaultList()

    }

    private fun applyDefaultList() {
        defaultList.forEach { item ->
            searchList.add(
                ModelItemMain(
                    item.id, item.image, item.name,
                    item.subtitle, null
                )
            )
        }
        adapterMain.setList(searchList)
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
                if (adapterMain.itemCount != defaultList.size) {
                    searchList.clear()
                    applyDefaultList()
                }
            }
            return true
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
            results = viewModel.selectedListName(query, newList)
            val listSize = searchList.size
            val resultSize = results.size
            if (listSize > resultSize) {
                // Search From List
                Log.i("mantul", "Search From List")
                for (position in searchList.size - 1 downTo 0) {
                    findMatchedItem(query, position)
                }
            } else {
                // Search From Results
                Log.i("mantul", "Search From Results")
                results.forEachIndexed { position, selectionItem ->
                    val searchedItem = searchList.find { it.id == selectionItem.id }
                    // Selection item add to list
                    if (searchedItem == null) {
                        searchList.add(position, selectionItem)
                        adapterMain.notifyItemInserted(position)
                        Log.i("mantul", "add ${selectionItem.name}")
                        update(query, position)
                    } else {
                        findItemToMoveOrUpdate(query, position)
                    }
                }
            }

        }
    }

    private fun findMatchedItem(query: String, position: Int) {
        val item = searchList[position]
        val selectionItem = selectionItem(item)
        val haveResult = selectionItem != null
        if (!haveResult) {
            remove(position)
        } else {
            findItemToMoveOrUpdate(query, position)
        }
    }

    private fun findItemToMoveOrUpdate(query: String, position: Int) {
        if (differentIndex(position)) {
            move(position, query)
            findMatchedItem(query, position)
        } else {
            // Update Query
            update(query, position)
        }
    }

    private fun selectionItem(item: ModelItemMain): ModelItemMain? {
        return results.find { it.id == item.id }
    }

    private fun differentIndex(position: Int): Boolean {
        val item = searchList[position]
        val result = results.find { it.id == item.id }
        val itemIndex = searchList.indexOf(item)
        val resultIndex = results.indexOf(result)
        return if (resultIndex >= 0) {
            itemIndex != resultIndex
        } else {
            false
        }
    }

    private fun remove(position: Int) {
        searchList.removeAt(position)
        adapterMain.notifyItemRemoved(position)
    }

    private fun move(position: Int, query: String) {
        val item = searchList[position]
        val fromPosition = searchList.indexOf(item)
        val selectionItem = selectionItem(item)
        val toPosition = results.indexOf(selectionItem)
        if (selectionItem != null) {
            searchList.remove(item)
            searchList.add(toPosition, selectionItem)
            adapterMain.notifyItemMoved(fromPosition, toPosition)
            // Update Query
            update(query, toPosition)
           //Log.d("mantul", "move: ${item.name} from $fromPosition to $toPosition")
        }
    }

    private fun update(query: String,  position: Int) {
        searchList[position].query = query
        adapterMain.notifyItemChanged(position)
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

    override fun onClickItem(item: ModelItemMain) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}