package com.ankit.filtersearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankit.filtersearch.adapter.SimpleRecyclerAdapter
import com.ankit.filtersearch.model.Post
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_recyclerview.*
import java.util.concurrent.TimeUnit

class SearchRecyclerviewActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private val disposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recyclerview)
      viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SimpleRecyclerAdapter(this, viewModel.oldFilteredPosts)


        searchInput
            .textChanges()
            .debounce(200, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel
                    .search(it.toString())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val diffResult = DiffUtil.calculateDiff(PostsDiffUtilCallback(viewModel.oldFilteredPosts, viewModel.filteredPosts))
                        viewModel.oldFilteredPosts.clear()
                        viewModel.oldFilteredPosts.addAll(viewModel.filteredPosts)
                        diffResult.dispatchUpdatesTo(recyclerView.adapter as SimpleRecyclerAdapter)
                    }.addTo(disposable)
            }.addTo(disposable)
    }
    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
    class PostsDiffUtilCallback(private val oldList: List<Post>, private val newList: List<Post>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true // for the sake of simplicity we return true here but it can be changed to reflect a fine-grained control over which part of our views are updated
    }

}
