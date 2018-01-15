`version 0.0.3`

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
if you using jcenter
repositories {
        jcenter()
}

implementation 'com.radityalabs.universaladapter:universal-adapter:0.0.3'
```

```
if  you using maven
maven {
  url 'https://dl.bintray.com/radityagumay/android/'
}
```

## How to use

```
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
            if (viewType == 1) {
                ViewHolder.FooViewHolder.inflate(parent)
            } else {
                ViewHolder.BarViewHolder.inflate(parent)
            }
        }, { vh, _, item ->
            if (item.type == 1) {
                (vh as ViewHolder.FooViewHolder).bind(item)
            } else {
                (vh as ViewHolder.BarViewHolder).bind(item)
            }
        }, { position ->
            if (adapter.items[position].type == 1) {
                ViewHolder.FOO_TYPE
            } else {
                ViewHolder.BAR_TYPE
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
}
```

## License

```
Copyright 2017 R Aditya Gumay

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, 
software distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and limitations under the License.
```
