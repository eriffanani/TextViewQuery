package com.erif.textviewquery.model

import com.erif.library.TextViewQuery

data class ModelItemMain (
    val id: Int,
    val image: Int,
    val name: String,
    val subtitle: String,
    var query: String? = null
)