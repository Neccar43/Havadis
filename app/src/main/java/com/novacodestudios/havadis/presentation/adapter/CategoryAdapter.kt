package com.novacodestudios.havadis.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.databinding.CategoryRowBinding
import com.novacodestudios.havadis.presentation.fragments.SearchFragment

class CategoryAdapter(private val categoryList: List<SearchFragment.CategoryItem>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: CategoryRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding=CategoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int =categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item=categoryList.get(position)
        holder.binding.apply {
            tvCategoryName.text=item.name
            ivCategory.setImageResource(item.imageResource)
        }
    }


}