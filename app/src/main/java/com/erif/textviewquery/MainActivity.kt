package com.erif.textviewquery

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erif.textviewquery.adapter.AdapterList
import com.erif.textviewquery.adapter.CountryRepo
import com.erif.textviewquery.adapter.ModelItemSearch
import com.erif.textviewquery.adapter.main.AdapterMain
import com.erif.textviewquery.adapter.main.ModelItemMain

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
        val recyclerView: RecyclerView = findViewById(R.id.act_main_recyclerViewQuery)
        val manager = LinearLayoutManager(this)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        recyclerView.isVisible = false

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
                if (notEmptyQuery) {
                    edSearch.setText("")
                    edSearch.clearFocus()
                    delayClick(200L) {
                        imm.hideSoftInputFromWindow(edSearch.windowToken, 0)
                    }
                }
            }
        }
        setupMainRecyclerView()
    }

    private fun setupMainRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.act_main_recyclerView)
        val adapterMain = AdapterMain()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterMain
        }
        val arrImage = arrayOf(
            R.mipmap.man1, R.mipmap.man2, R.mipmap.man3,
            R.mipmap.women1, R.mipmap.women2, R.mipmap.women3,
            R.mipmap.man1, R.mipmap.man2, R.mipmap.man3,
            R.mipmap.women1, R.mipmap.women2, R.mipmap.women3,
            R.mipmap.man1, R.mipmap.man2, R.mipmap.man3,
            R.mipmap.women1, R.mipmap.women2, R.mipmap.women3
        )
        val listMain: MutableList<ModelItemMain> = ArrayList()
        arrImage.forEachIndexed { idx, item ->
            listMain.add(
                ModelItemMain(idx, item, "This is Name $idx", "Subtitle from name $idx")
            )
        }
        adapterMain.setList(listMain)
    }

    private fun delayClick(duration: Long = 350L, execute: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            execute()
        }
        handler.postDelayed(runnable, duration)
    }

    private fun getSelectedList(query: String?): MutableList<String>  {
        val newList: MutableList<String> = ArrayList()
        val max = 8
        val mQuery = query ?: ""
        val emptyQuery = TextUtils.isEmpty(query)
        val blankQuery = mQuery.isBlank()
        if (!emptyQuery && !blankQuery) {
            val firstCharQuery = mQuery[0]
            val results: List<String> = countries.filter {
                it.startsWith(mQuery, true) or it.contains(mQuery, true)
            }
            val finalResults: MutableList<String> = results.toMutableList()
            results.forEachIndexed { idx, result ->
                val resultFirstChar = result[0]
                if (resultFirstChar.equals(firstCharQuery, true)) {
                    finalResults.removeAt(idx)
                    finalResults.add(0, result)
                }
            }
            newList.addAll(
                if(finalResults.size > max)
                    finalResults.slice(0 ..< max)
                else
                    finalResults
            )
        }
        return newList
    }

}