package com.novacodestudios.havadis.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.novacodestudios.havadis.data.remote.dto.sourceDto.Source
import com.novacodestudios.havadis.databinding.SourceRowBinding

class InnerSourceAdapter(private val sources: List<Source>) :
    RecyclerView.Adapter<InnerSourceAdapter.InnerSourceViewHolder>() {

    inner class InnerSourceViewHolder( val binding: SourceRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerSourceViewHolder {
        val binding=SourceRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return InnerSourceViewHolder(binding)
    }

    override fun getItemCount(): Int =sources.size

    override fun onBindViewHolder(holder: InnerSourceViewHolder, position: Int) {
       holder.binding.apply {
           tvSourceName.text=sources.get(position).name
       }
    }

}