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

    private lateinit var adapter: UniversalAdapter<Foo, RecyclerView.ViewHolder>

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
            if (viewType == 1) { FooViewHolder.inflate(parent) } else { BarViewHolder.inflate(parent) }
        }, { vh, _, item ->
            if (item.type == 1) { (vh as FooViewHolder).bind(item) } else { (vh as BarViewHolder).bind(item) }
        }, { position ->
            if (adapter.items[position].type == 1) { 1 } else { 2 }
        })

        with(recycle) {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initData() {
        adapter.addAll(datas())
    }

    private fun datas(): List<Foo> {
        val items = mutableListOf<Foo>()
        items.add(Foo("Title BAAR", "BAAR", 1))
        return (0 until 10).mapTo(items) { Foo("Title $it", "Message $it", 2) }
    }
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
}

data class Foo(val title: String,
               val message: String,
               val type: Int)