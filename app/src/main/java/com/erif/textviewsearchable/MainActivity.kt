package com.erif.textviewsearchable

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erif.textviewsearchable.adapter.AdapterList
import com.erif.textviewsearchable.adapter.CountryRepo
import com.erif.textviewsearchable.adapter.ModelItemSearch

class MainActivity : AppCompatActivity() {

    private val adapter = AdapterList()
    private val countries = CountryRepo.countries()
    private var resultSearch: MutableList<ModelItemSearch> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val icSearch: ImageView = findViewById(R.id.act_main_icSearch)
        val btnClear: RelativeLayout = findViewById(R.id.act_main_btnClear)
        btnClear.isVisible = false
        //val icClear: ImageView = findViewById(R.id.act_main_icClear)

        val cardView: CardView = findViewById(R.id.act_main_cardView)
        val recyclerView: RecyclerView = findViewById(R.id.act_main_recyclerView)
        val manager = LinearLayoutManager(this)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        recyclerView.isVisible = false

        val edSearch: EditText = findViewById(R.id.act_main_edSearch)
        edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                val emptyQuery = TextUtils.isEmpty(query)
                val blankQuery = query.isBlank()
                if (!emptyQuery && !blankQuery) {
                    val results = getSelectedList(query)
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
                    if (!recyclerView.isVisible) {
                        recyclerView.isVisible = true
                        cardView.elevation = resources.getDimension(R.dimen.card_elevation)
                    }
                } else{
                    recyclerView.isVisible = false
                    cardView.elevation = 0f
                }
                adapter.setList(resultSearch)
            }

        })

        btnClear.setOnClickListener {
            val notEmptyQuery = !TextUtils.isEmpty(edSearch.text.toString())
            delayClick {
                if (notEmptyQuery)
                    edSearch.setText("")
            }
        }
    }

    private fun delayClick(execute: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            execute()
        }
        handler.postDelayed(runnable, 300L)
    }

    private fun getSelectedList(query: String?): MutableList<String>  {
        val newList: MutableList<String> = ArrayList()
        val max = 8
        if (!TextUtils.isEmpty(query) && query != null) {
            val results: List<String> = countries.filter { it.contains(query, ignoreCase = true) }
            newList.addAll(
                if(results.size > max)
                    results.slice(0 ..< max)
                else
                    results
            )
        }
        return newList
    }

}