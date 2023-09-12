package com.erif.textviewquery.usecases

import com.erif.textviewquery.model.ModelItemMain

interface MainListItemListener {
    fun onClickItem(item: ModelItemMain)
}