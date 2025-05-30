package com.kurly.kjm.kurlytest.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.kurly.kjm.kurlytest.data.SectionProductData
import com.kurly.kjm.kurlytest.data.SectionWithProducts
import com.kurly.kjm.kurlytest.databinding.ItemSectionsBinding

abstract class BaseSectionViewHolder(
    private val binding: ItemSectionsBinding,
    private val layoutManagerFactory: (RecyclerView) -> RecyclerView.LayoutManager
) : RecyclerView.ViewHolder(binding.root) {

    private var sectionAdapter: SectionItemAdapter? = null

    init {
        val pool = RecyclerView.RecycledViewPool()
        binding.rvSectionItems.apply {
            setRecycledViewPool(pool)
            itemAnimator = null
        }
    }

    fun bind(entry: SectionWithProducts, loadItems: (SectionWithProducts) -> List<SectionProductData>) {
        binding.tvSectionTitle.text = entry.section.title

        binding.rvSectionItems.layoutManager = layoutManagerFactory(binding.rvSectionItems)
        if (sectionAdapter == null) {
            sectionAdapter = SectionItemAdapter(entry.section.type).apply {
                setHasStableIds(true)
            }
            binding.rvSectionItems.adapter = sectionAdapter
        }

        val list = loadItems(entry)
        val displayItems = if (entry.section.type == "grid") list.take(6) else list
        sectionAdapter!!.submitList(displayItems)
    }
}