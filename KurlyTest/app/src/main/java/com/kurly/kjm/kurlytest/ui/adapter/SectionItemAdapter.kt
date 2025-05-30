package com.kurly.kjm.kurlytest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kurly.kjm.kurlytest.BindingAdapters.setFavoriteIcon
import com.kurly.kjm.kurlytest.data.SectionProductData
import com.kurly.kjm.kurlytest.databinding.ItemProductBinding
import com.kurly.kjm.kurlytest.databinding.ItemProductVerticalBinding

class SectionItemAdapter(private val type: String?)
    : ListAdapter<SectionProductData, RecyclerView.ViewHolder>(SectionItemDiffCallback){

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL   = 1
        const val GRID       = 2

        private val SectionItemDiffCallback = object : DiffUtil.ItemCallback<SectionProductData>() {
            override fun areItemsTheSame(
                oldItem: SectionProductData,
                newItem: SectionProductData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SectionProductData,
                newItem: SectionProductData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when(type) {
        "horizontal" -> HORIZONTAL
        "vertical"   -> VERTICAL
        "grid"       -> GRID
        else         -> VERTICAL
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            HORIZONTAL, GRID -> {
                val binding = ItemProductBinding.inflate(inflater, parent, false)
                HorizontalGridViewHolder(binding)
            }
            VERTICAL -> {
                val binding = ItemProductVerticalBinding.inflate(inflater, parent, false)
                VerticalViewHolder(binding)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder) {
            is HorizontalGridViewHolder -> holder.bind(item)
            is VerticalViewHolder -> holder.bind(item)
        }
    }

    inner class HorizontalGridViewHolder(val binding: ItemProductBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(d: SectionProductData) {
            binding.product = d

            binding.btnFavorite.apply {
                setOnClickListener {
                    d.isFavorite = !d.isFavorite
                    setFavoriteIcon(d.isFavorite)
                }
            }
            binding.executePendingBindings()
        }
    }
    inner class VerticalViewHolder(val binding: ItemProductVerticalBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(d: SectionProductData) {
            binding.product = d

            binding.btnFavorite.apply {
                setOnClickListener {
                    d.isFavorite = !d.isFavorite
                    setFavoriteIcon(d.isFavorite)
                }
            }
            binding.executePendingBindings()
        }
    }
}