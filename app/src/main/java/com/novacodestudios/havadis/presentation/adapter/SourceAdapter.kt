package com.novacodestudios.havadis.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.novacodestudios.havadis.data.remote.dto.sourceDto.Source
import com.novacodestudios.havadis.databinding.CategoryRowBinding
import com.novacodestudios.havadis.databinding.SourceCategoryRowBinding
import com.novacodestudios.havadis.databinding.SourceRowBinding
import java.util.Locale

class SourceAdapter(private val groupedSources: Map<String, List<Source>>) :
    RecyclerView.Adapter<SourceAdapter.SourceViewHolder>() {


    inner class SourceViewHolder(val binding: SourceCategoryRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val binding =
            SourceCategoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SourceViewHolder(binding)
    }

    override fun getItemCount(): Int = groupedSources.size

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val category = groupedSources.keys.toList()[position]
        val sources = groupedSources[category]!!
        holder.binding.apply {
            tvCategory.text =
                category.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            rvSourceCategory.adapter = InnerSourceAdapter(sources)
        }
    }


}
