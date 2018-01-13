`version 0.0.2`

## Summary

The most boilerplate piece in developing application in Android is create a List UI Component. To implement List UI Component we have few ceremonies such as:
1. Create a List widget (in this case RecycleView)
2. Create a Adapter
3. Create a ViewHolder
4. Create a List Item widget for ViewHolder

Doing a repetitive code is not DRY at all. We wasting time to create such things. In that time, i was thinking how to reduce amount of class in doing create List UI Component.

Blog here https://medium.com/@gumay.raditya/universal-adapter-for-android-2f4f07cdae53

## Installation

* Add the following to your project level `build.gradle`:

```
implementation 'com.radityalabs.universaladapter:universal-adapter:0.0.2'
```

```
maven {
  url 'https://dl.bintray.com/radityagumay/android/'
}
```

## How to use

```
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
```
