package com.erif.textviewquery.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.erif.textviewquery.R
import com.erif.textviewquery.adapter.main.ModelItemMain

class ActMainViewModel: ViewModel() {

    fun mainData(): MutableList<ModelItemMain> {
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
        return listMain
    }

    fun selectedList(query: String?, countries: Array<String>): MutableList<String>  {
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

    fun delay(duration: Long = 350L, execute: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            execute()
        }
        handler.postDelayed(runnable, duration)
    }

}