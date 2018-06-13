package com.radityalabs.universaladapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface Universal<in T> {
    fun add(item: T)

    fun add(item: T, index: Int)

    fun addAll(collection: Collection<T>, index: Int)

    fun addAll(collection: Collection<T>)

    fun safeAddAll(collection: Collection<T>?)

    fun remove(item: T)

    fun removeRange(collection: Collection<T>)

    fun removeRange(vararg item: T)

    fun update(item: T)

    fun update(index: Int, item: T)

    fun update(index: Int)

    fun updateAll(collection: Collection<T>)

    fun safeUpdateAll(collection: Collection<T>?)

    fun updateRange(collection: Collection<T>)

    fun updateRange(vararg item: T)

    fun clear()

    fun clearAndAddAll(collection: Collection<T>)
}

open class UniversalAdapter<T, VH : RecyclerView.ViewHolder>(
        private val onCreateViewHolder: (parent: ViewGroup, viewType: Int) -> VH,
        private val onBindViewHolder: (viewHolder: VH, position: Int, item: T) -> Unit,
        private val onViewType: ((viewType: Int) -> Int)? = null,
        private val infinite: Boolean = false,
        private val onDetachedFromWindow: ((VH) -> Unit)? = null) :
        RecyclerView.Adapter<VH>(), Universal<T> {

    operator fun invoke(block: UniversalAdapter<T, VH>.() -> Unit) {
        block()
    }

    val itemSize: Int
        get() = items.size

    var items = mutableListOf<T>()
        private set

    var onGetItemViewType: ((position: Int) -> Int)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
            onCreateViewHolder.invoke(parent, viewType)

    override fun getItemCount(): Int = if (infinite) Int.MAX_VALUE else items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        var newPosition = position
        if (infinite) {
            newPosition = position % items.size
        }
        val item = items[newPosition]
        onBindViewHolder.invoke(holder, newPosition, item)
    }

    override fun getItemViewType(position: Int): Int {
        var newPosition = position
        if (infinite) {
            newPosition = position % items.size
        }
        return if (onViewType != null) {
            onViewType.invoke(newPosition)
        } else {
            val onGetItemViewType = onGetItemViewType
            onGetItemViewType?.invoke(newPosition) ?: super.getItemViewType(newPosition)
        }
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
        onDetachedFromWindow?.invoke(holder)
    }

    override fun add(item: T, index: Int) {
        items.add(index, item)
        notifyItemInserted(index)
    }

    override fun add(item: T) {
        items.add(item)
        notifyItemInserted(items.size)
    }

    override fun addAll(collection: Collection<T>, index: Int) {
        items.addAll(index, collection)
        notifyItemRangeInserted(index, items.size)
    }

    override infix fun clearAndAddAll(collection: Collection<T>) {
        items.clear()
        addAll(collection)
    }

    override fun addAll(collection: Collection<T>) {
        items.addAll(collection)
        notifyDataSetChanged()
    }

    override fun safeAddAll(collection: Collection<T>?) {
        collection?.let {
            items.addAll(collection)
            notifyDataSetChanged()
        }
    }

    /* remove only single item */
    override fun remove(item: T) {
        val index = items.indexOfFirst { it == item }
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    /* this use for example range of collection from n to t checked */
    override fun removeRange(collection: Collection<T>) {
        collection.forEachIndexed { index, item ->
            items.remove(item)
            notifyItemRemoved(index)
        }
    }

    /* this use for example collection but in difference order */
    override fun removeRange(vararg item: T) {
        for (i in item) {
            remove(i)
        }
    }

    override fun update(index: Int, item: T) {
        notifyItemChanged(index, item)
    }

    override fun update(index: Int) {
        notifyItemChanged(index)
    }

    override fun update(item: T) {
        val index = items.indexOfFirst { it == item }
        notifyItemChanged(index)
    }

    override fun updateAll(collection: Collection<T>) {
        collection.forEachIndexed { position, item ->
            items[position] = item
        }
        notifyDataSetChanged()
    }

    override fun safeUpdateAll(collection: Collection<T>?) {
        collection?.forEachIndexed { position, item ->
            items[position] = item
        }
        notifyDataSetChanged()
    }

    override fun updateRange(collection: Collection<T>) {
        collection.forEachIndexed { index, item ->
            update(index, item)
        }
    }

    override fun updateRange(vararg item: T) {
        item.forEachIndexed { index, i ->
            update(index, i)
        }
    }

    override fun clear() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }
}