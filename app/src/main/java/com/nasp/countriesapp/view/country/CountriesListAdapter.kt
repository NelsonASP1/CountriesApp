package com.nasp.countriesapp.view.country

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nasp.countriesapp.R
import com.nasp.countriesapp.databinding.LayoutItemCountryBinding
import com.nasp.countriesapp.room.model.CountriesEntity

class CountriesListAdapter(
    private val context: Context,
    private val binder: CountriesAdapterBinder,
    items: List<CountriesEntity>
) :
    RecyclerView.Adapter<CountriesListAdapter.ViewHolder>(), Filterable {
    class ViewHolder(val binding: LayoutItemCountryBinding) : RecyclerView.ViewHolder(binding.root)

    private val itemsBitmapsMap = mutableListOf<CountriesWrapper>().apply {
        items.forEach {
            add(CountriesWrapper(it, null))
        }
    }.toMutableList()

    private var filteredItemsBitmapsMap = itemsBitmapsMap.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_item_country,
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredItemsBitmapsMap[position]

        holder.binding.item = item.countriesEntity

        if (item.flagBitmap != null) {
            holder.binding.flagImage.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.transparent
                )
            )
            holder.binding.flagImage.setImageBitmap(item.flagBitmap)
        } else {
            holder.binding.flagImage.setImageBitmap(null)
            holder.binding.flagImage.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.black_transparent
                )
            )
        }

        holder.binding.root.setOnClickListener { binder.openDetails(item.countriesEntity, item.flagBitmap) }
    }

    override fun getItemCount() = filteredItemsBitmapsMap.size

    fun setImageBitmap(bitmapPair: Pair<Long, Bitmap>) {
        itemsBitmapsMap
            .asSequence()
            .withIndex()
            .filter { it.value.flagBitmap == null }
            .firstOrNull { it.value.countriesEntity.id == bitmapPair.first }
            ?.let {
                it.value.flagBitmap = bitmapPair.second
                notifyItemChanged(it.index)

            }

    }

    fun filter(string: String) {
        filter.filter(string)
    }

    class FilterCountry(
        private val completeList: List<CountriesWrapper>,
        private val onFiltered: (List<CountriesWrapper>) -> Unit
    ) :
        Filter() {

        override fun performFiltering(constraint: CharSequence): FilterResults {
            val characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]"
            val emojiLess = constraint.replace(Regex(characterFilter), "")

            val strings = emojiLess
                .split(",")
                .map { it.trim() }
                .filterNot { it.isBlank() }

            val filteredList = completeList.filter { countryWrapper ->
                strings.any { countryWrapper.countriesEntity.name == it }
            }


            return FilterResults().apply {
                values = filteredList
                count = filteredList.size
            }
        }

        @Suppress("unchecked_cast")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            onFiltered(results!!.values as List<CountriesWrapper>)
        }

    }

    override fun getFilter() = FilterCountry(itemsBitmapsMap) { newList ->
        if (newList.isEmpty()) {
            binder.onNoCountry()
        } else {
            binder.onCountriesResults(newList)
        }

        val oldList = filteredItemsBitmapsMap.toList()

        changeLists(oldList, newList)

        this.filteredItemsBitmapsMap = newList.toMutableList()
    }

    private fun changeLists(oldList: List<CountriesWrapper>, newList: List<CountriesWrapper>) {
        if (oldList.size > newList.size) {
            notifyItemRangeRemoved(newList.size, oldList.size)

        } else if (oldList.size < newList.size) {
            notifyItemRangeInserted(oldList.size, newList.size)

        }

        newList.zip(oldList).forEach {
            if (it.first != it.second) {
                notifyItemChanged(newList.indexOf(it.first))
            }
        }
    }

    fun restoreFilter() {
        binder.onCountriesResults(itemsBitmapsMap)

        val oldList = filteredItemsBitmapsMap.toList()

        changeLists(oldList, itemsBitmapsMap)

        this.filteredItemsBitmapsMap = itemsBitmapsMap
    }
}