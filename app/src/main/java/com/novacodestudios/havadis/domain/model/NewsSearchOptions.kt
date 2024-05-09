package com.novacodestudios.havadis.domain.model

import com.novacodestudios.havadis.util.Language

data class NewsSearchOptions(
    val sources: String? = null,
    val domains: String? = null,
    val language: Language? = null,
    val searchQuery: String,
)

