package com.erif.textviewquery.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erif.textviewquery.R
import com.erif.textviewquery.databinding.ActivityMainBinding
import com.erif.textviewquery.model.ModelItemSearch
import com.erif.textviewquery.ui.adapter.serach.AdapterCountry
import com.erif.textviewquery.ui.adapter.serach.AdapterSearch
import com.erif.textviewquery.ui.adapter.serach.CountryRepo
import com.erif.textviewquery.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val adapterCountry = AdapterCountry()
    private val adapterQuery = AdapterSearch()
    private val countries = CountryRepo.countries()
    private var resultSearch: MutableList<ModelItemSearch> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        // Main RecyclerView
        recyclerView = binding.actMainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterCountry
        }
        adapterCountry.setList(CountryRepo.countries().toMutableList())

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
                val emptyColor = "#B4B4B4".toColorInt()
                val fillColor = "#595959".toColorInt()
                val searchIconColor = if (emptyQuery) fillColor else emptyColor
                ImageViewCompat.setImageTintList(icSearch, ColorStateList.valueOf(searchIconColor))
            }

            override fun afterTextChanged(s: Editable?) {
                if (resultSearch.isNotEmpty()) {
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