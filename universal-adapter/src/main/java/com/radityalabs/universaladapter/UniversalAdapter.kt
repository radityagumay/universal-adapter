package com.radityalabs.universaladapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface Universal<in T> {
    fun addAll(items: List<T>)

    fun add(item: T)

    fun update(item: T)

    fun updateRange(vararg items: T)

    fun remove(item: T, position: Int)

    fun removeRange(vararg items: T)

    fun clearAndAddAll(collection: List<T>)
}

class UniversalAdapter<T, VH : RecyclerView.ViewHolder>(
        private val onCreateViewHolder: (ViewGroup?, Int) -> VH,
        private val onBindViewHolder: (VH, Int, T) -> Unit,
        private val onViewType: ((Int) -> Int)? = null) : RecyclerView.Adapter<VH>(),
        Universal<T> {

    var items = mutableListOf<T>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH =
            onCreateViewHolder.invoke(parent, viewType)

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder.invoke(holder, position, items[position])
    }

    override fun getItemViewType(position: Int) = onViewType?.invoke(position) ?: super.getItemViewType(position)

    override fun getItemCount() = items.size

    override fun addAll(items: List<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun add(item: T) {
        this.items.add(item)
        notifyItemInserted(this.items.size)
    }

    override fun remove(item: T, position: Int) {
        this.items.remove(item)
        notifyItemRemoved(position)
    }

    override fun removeRange(vararg items: T) {
        items.forEachIndexed { index, item -> remove(item, index) }
    }

    override fun update(item: T) {
        items.forEachIndexed { index, i ->
            if (i == item) {
                items[index] = item
                notifyItemChanged(index)
            }
        }
    }

    override fun updateRange(vararg items: T) {
        for (i in 0 until this.items.size) {
            (0 until items.size)
                    .filter { i == it }
                    .forEach {
                        this.items[i] = items[it]
                        update(this.items[i])
                    }
        }
    }

    override fun clearAndAddAll(collection: List<T>) {
        items.clear()
        addAll(collection)
    }
}