package com.kurly.kjm.kurlytest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.kurly.kjm.kurlytest.data.SectionProductData
import com.kurly.kjm.kurlytest.data.SectionWithProducts
import com.kurly.kjm.kurlytest.databinding.ItemSectionsBinding

class SectionAdapter(private val loadItems: (SectionWithProducts) -> List<SectionProductData>)
    : ListAdapter<SectionWithProducts, RecyclerView.ViewHolder>(SectionDiffCallback) {

    companion object {
        private val SectionDiffCallback = object : DiffUtil.ItemCallback<SectionWithProducts>()
        {
            override fun areItemsTheSame(
                oldItem: SectionWithProducts,
                newItem: SectionWithProducts
            ): Boolean {
                return oldItem.section.id == newItem.section.id
            }

            override fun areContentsTheSame(
                oldItem: SectionWithProducts,
                newItem: SectionWithProducts)
            : Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class HorizontalSectionViewHolder(binding: ItemSectionsBinding)
        : BaseSectionViewHolder (binding, { rv ->
        LinearLayoutManager(rv.context, HORIZONTAL, false)
    })

    inner class VerticalSectionViewHolder(binding: ItemSectionsBinding)
        : BaseSectionViewHolder (binding, { rv ->
        LinearLayoutManager(rv.context, VERTICAL, false)
    })

    inner class GridSectionViewHolder(binding: ItemSectionsBinding)
        : BaseSectionViewHolder (binding, { rv ->
        GridLayoutManager(rv.context, 3, VERTICAL, false)
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSectionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when(viewType) {
            SectionItemAdapter.HORIZONTAL -> HorizontalSectionViewHolder(binding)
            SectionItemAdapter.VERTICAL -> VerticalSectionViewHolder(binding)
            SectionItemAdapter.GRID -> GridSectionViewHolder(binding)
            else -> VerticalSectionViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as BaseSectionViewHolder).bind(item, loadItems)
    }

    override fun getItemViewType(position: Int) =
        when (getItem(position).section.type) {
            "horizontal" -> SectionItemAdapter.HORIZONTAL
            "vertical"   -> SectionItemAdapter.VERTICAL
            "grid"       -> SectionItemAdapter.GRID
            else         -> SectionItemAdapter.VERTICAL
        }
}