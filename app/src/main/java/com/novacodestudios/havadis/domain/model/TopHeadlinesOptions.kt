package com.novacodestudios.havadis.domain.model

import com.novacodestudios.havadis.util.Category
import com.novacodestudios.havadis.util.Country

data class TopHeadlinesOptions(
    val country: Country? = null,
    val category: Category? = null,
    val sources: String? = null,
    val searchQuery: String? = null,
)