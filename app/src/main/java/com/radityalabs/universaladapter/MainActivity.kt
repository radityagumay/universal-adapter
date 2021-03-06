package com.radityalabs.universaladapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_bar_item.view.*
import kotlinx.android.synthetic.main.activity_main_item.view.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UniversalAdapter<ViewHolder.Foo, RecyclerView.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        adapter = UniversalAdapter({ parent, viewType ->
            when (viewType) {
                1 -> ViewHolder.FooViewHolder.inflate(parent)
                else -> ViewHolder.BarViewHolder.inflate(parent)
            }
        }, { vh, _, item ->
            when (vh) {
                is ViewHolder.FooViewHolder -> vh.bind(item)
                is ViewHolder.BarViewHolder -> vh.bind(item)
            }
        }, { position ->
            when (adapter.items[position].type) {
                1 -> ViewHolder.FOO_TYPE
                else -> ViewHolder.BAR_TYPE
            }
        }, onDetachedFromWindow = { vh ->
            when (vh) {
                is ViewHolder.FooViewHolder -> vh.unBind()
                is ViewHolder.BarViewHolder -> vh.unBind()
            }
        })

        recycle.apply {
            adapter = this@MainActivity.adapter
        }.vertical()
    }

    private fun initData() {
        adapter.addAll(datas())
    }

    private fun datas(): List<ViewHolder.Foo> {
        val items = mutableListOf<ViewHolder.Foo>()
        items.add(ViewHolder.Foo("Title BAAR", "BAAR", 1))
        return (0 until 10).mapTo(items) { ViewHolder.Foo("Title $it", "Message $it", 2) }
    }
}

fun RecyclerView.vertical() {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
}

sealed class ViewHolder {
    companion object {
        val FOO_TYPE = 1
        val BAR_TYPE = 1
    }

    class FooViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun inflate(parent: ViewGroup?) =
                    FooViewHolder(LayoutInflater.from(parent?.context)
                            .inflate(R.layout.activity_main_item, parent, false))
        }

        private val title = view.title
        private val message = view.message

        fun bind(item: Foo) {
            title.text = item.title
            message.text = item.message
        }

        fun unBind() {}
    }

    class BarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun inflate(parent: ViewGroup?) =
                    BarViewHolder(LayoutInflater.from(parent?.context)
                            .inflate(R.layout.activity_main_bar_item, parent, false))
        }

        private val message = view.barbar

        fun bind(item: Foo) {
            message.text = item.message
        }

        fun unBind() {}
    }

    data class Foo(val title: String,
                   val message: String,
                   val type: Int)
}