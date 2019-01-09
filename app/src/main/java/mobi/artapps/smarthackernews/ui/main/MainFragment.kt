package mobi.artapps.smarthackernews.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import mobi.artapps.smarthackernews.R
import mobi.artapps.smarthackernews.model.local.entity.News

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


        val swipeRefreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeRefreshLayout?.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );

        swipeRefreshLayout?.setOnRefreshListener {
            viewModel.invalidateDataSource()
        }

        val recyclerView = view?.findViewById<RecyclerView>(R.id.main_fragment_recycler)
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        newsAdapter = NewsAdapter()
        recyclerView?.adapter = newsAdapter

        viewModel.mAllNews.observe(this, Observer<PagedList<News>> {
            Log.d("Activity", "list: ${it?.size}")
            //showEmptyList(it?.size == 0)
            newsAdapter.submitList(it)
            swipeRefreshLayout?.isRefreshing = false
        })

        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(context, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })

        viewModel.searchRepo("")
    }

}
