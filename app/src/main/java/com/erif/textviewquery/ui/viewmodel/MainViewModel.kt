package com.erif.textviewquery.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.erif.textviewquery.R
import com.erif.textviewquery.model.ModelItemMain

class MainViewModel: ViewModel() {

    val arrName = arrayOf(
        "Darrell C. Bennett", "Edgar C. Taylor", "Steve J. Machin",
        // Women
        "Abigail Purcell", "Angelina Minchin", "Bianca McCloughry",
        "Simon A. Miller", "Thomas Fraser", "Louie Summers"
        // Women
        ,"Geraldine E. Helmick", "Lisa G. Clark", "Brittany T. Shrader",
        "Gabriel Thorby", "Joseph Kinsella", "Xavier Pownall",
        // Women
        "Hannah Harding", "Jessica Moore", "Leah Porter"
    )

    fun mainData(): MutableList<ModelItemMain> {
        val arrImage = arrayOf(
            R.mipmap.man1, R.mipmap.man2, R.mipmap.man3,
            R.mipmap.women1, R.mipmap.women2, R.mipmap.women3,
            R.mipmap.man1, R.mipmap.man2, R.mipmap.man3,
            R.mipmap.women1, R.mipmap.women2, R.mipmap.women3,
            R.mipmap.man1, R.mipmap.man2, R.mipmap.man3,
            R.mipmap.women1, R.mipmap.women2, R.mipmap.women3
        )
        val arrProfession = arrayOf(
            "Lawyer", "Pathologist", "Meter reader",
            "Childminder", "Judge", "Television presenter",
            "Handyman", "Window cleaner", "Coastguard",
            "Pilot", "Landscape gardener", "Blacksmith",
            "Refuse collector", "Mathematician", "Market trader",
            "Artist", "Social Media Influencer", "Art historian"
        )
        val listMain: MutableList<ModelItemMain> = ArrayList()
        arrImage.forEachIndexed { idx, item ->
            listMain.add(
                ModelItemMain(idx, item, arrName[idx], arrProfession[idx])
            )
        }
        return listMain
    }

    fun selectedList(query: String?, countries: Array<String>): MutableList<String>  {
        val newList: MutableList<String> = ArrayList()
        val max = 7
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
                if (mQuery.length == 1) {
                    val resultFirstChar = result[0]
                    if (resultFirstChar.equals(firstCharQuery, true)) {
                        finalResults.removeAt(idx)
                        finalResults.add(0, result)
                    }
                } else {
                    if (result.length >= mQuery.length) {
                        val resultChars = result.substring(0, mQuery.length)
                        if (mQuery.equals(resultChars, true)) {
                            finalResults.removeAt(idx)
                            finalResults.add(0, result)
                        }
                    }
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

    fun selectedListName(query: String?, listToFilter: List<ModelItemMain>): MutableList<ModelItemMain>  {
        val newList: MutableList<ModelItemMain> = ArrayList()
        val max = 7
        val mQuery = query ?: ""
        val emptyQuery = TextUtils.isEmpty(query)
        val blankQuery = mQuery.isBlank()
        if (!emptyQuery && !blankQuery) {
            val firstCharQuery = mQuery[0]
            val results: List<ModelItemMain> = listToFilter.filter {
                it.name.startsWith(mQuery, true) or it.name.contains(mQuery, true)
            }
            val finalResults: MutableList<ModelItemMain> = results.toMutableList()
            results.forEachIndexed { idx, result ->
                if (mQuery.length == 1) {
                    val resultFirstChar = result.name[0]
                    if (resultFirstChar.equals(firstCharQuery, true)) {
                        finalResults.removeAt(idx)
                        finalResults.add(0, result)
                    }
                } else {
                    if (result.name.length >= mQuery.length) {
                        val resultChars = result.name.substring(0, mQuery.length)
                        if (mQuery.equals(resultChars, true)) {
                            finalResults.removeAt(idx)
                            finalResults.add(0, result)
                        }
                    }
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