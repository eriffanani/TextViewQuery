package com.erif.textviewquery.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erif.textviewquery.R
import com.erif.textviewquery.adapter.AdapterList
import com.erif.textviewquery.adapter.CountryRepo
import com.erif.textviewquery.adapter.ModelItemSearch
import com.erif.textviewquery.adapter.main.AdapterMain
import com.erif.textviewquery.databinding.ActivityMainBinding
import com.erif.textviewquery.ui.viewmodel.ActMainViewModel

class MainActivity : AppCompatActivity() {

    private val adapterMain = AdapterMain()
    private val adapterQuery = AdapterList()
    private val countries = CountryRepo.countries()
    private var resultSearch: MutableList<ModelItemSearch> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ActMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Main RecyclerView
        recyclerView = binding.actMainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterMain
        }
        adapterMain.setList(viewModel.mainData())

        val cardSearch = binding.actMainCardView
        val icSearch = binding.actMainIcSearch
        val edSearch = binding.actMainEdSearch
        // Query RecyclerView
        val recyclerViewQuery = binding.actMainRecyclerViewQuery.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterQuery
            isVisible = false
        }
        val btnClear = binding.actMainBtnClear.apply {
            isVisible = false
            setOnClickListener {
                val notEmptyQuery = !TextUtils.isEmpty(edSearch.text.toString())
                viewModel.delay {
                    if (notEmptyQuery) {
                        edSearch.setText("")
                        edSearch.clearFocus()
                        viewModel.delay(200L) {
                            imm.hideSoftInputFromWindow(edSearch.windowToken, 0)
                        }
                    }
                }
            }
        }

        edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                val emptyQuery = TextUtils.isEmpty(query)
                val blankQuery = query.isBlank()
                if (!emptyQuery && !blankQuery) {
                    val results = viewModel.selectedList(query, countries)
                    resultSearch.clear()
                    results.forEachIndexed { idx, value ->
                        resultSearch.add(
                            ModelItemSearch(idx, query, value)
                        )
                    }
                } else if (emptyQuery) {
                    resultSearch.clear()
                }
                btnClear.isVisible = !emptyQuery
                val emptyColor = Color.parseColor("#B4B4B4")
                val fillColor = Color.parseColor("#595959")
                val searchIconColor = if (emptyQuery) fillColor else emptyColor
                ImageViewCompat.setImageTintList(icSearch, ColorStateList.valueOf(searchIconColor))
            }

            override fun afterTextChanged(s: Editable?) {
                if (resultSearch.size > 0) {
                    if (!recyclerViewQuery.isVisible) {
                        recyclerViewQuery.isVisible = true
                        cardSearch.elevation = resources.getDimension(R.dimen.card_elevation)
                    }
                } else{
                    recyclerViewQuery.isVisible = false
                    cardSearch.elevation = 0f
                }
                adapterQuery.setList(resultSearch)
            }
        })
    }

}