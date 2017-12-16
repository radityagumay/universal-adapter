package com.radityalabs.universaladapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface Universal<in T> {
    fun addAll(items: List<T>)

    fun safeAddAll(items: List<T>?)

    fun add(item: T)

    fun safeAdd(item: T?)

    fun remove(item: T)

    fun safeRemove(item : T?)
}

class UniversalAdapter<T : Any, VH : RecyclerView.ViewHolder>(
        private val onCreateViewHolder: (ViewGroup?, Int) -> VH,
        private val onBindViewHolder: (VH, Int, T) -> Unit,
        private val onViewType: ((viewType: Int) -> Int)? = null) : RecyclerView.Adapter<VH>(),
        Universal<T> {

    var items = mutableListOf<T>()
        private set

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, position, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH =
            onCreateViewHolder.invoke(parent, viewType)

    override fun getItemViewType(position: Int): Int {
        return onViewType?.invoke(position) ?: super.getItemViewType(position)
    }

    override fun getItemCount() = items.size

    override fun addAll(items: List<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun safeAddAll(items: List<T>?) {
        items?.let {
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun add(item: T) {
        this.items.add(item)
        notifyItemInserted(this.items.size)
    }

    override fun safeAdd(item: T?) {
        item?.let {
            this.items.add(item)
            notifyItemInserted(this.items.size)
        }
    }

    override fun remove(item: T) {
        this.items.remove(item)
    }

    override fun safeRemove(item: T?) {
        item?.let {
            this.items.remove(item)
        }
    }
}