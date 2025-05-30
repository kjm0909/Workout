package com.kurly.kjm.kurlytest.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kurly.kjm.kurlytest.R
import com.kurly.kjm.kurlytest.databinding.ActivityMainBinding
import com.kurly.kjm.kurlytest.ui.adapter.SectionAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    private lateinit var listAdapter: SectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.refreshSections()

        initListener()
        initView()
    }

    private fun initListener() {
        binding.rvMainList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            listAdapter = SectionAdapter(viewModel::loadItemsForSection)
            adapter = listAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy <= 0) return

                    val lm = recyclerView.layoutManager as? LinearLayoutManager ?: return

                    val lastVisiblePos = lm.findLastVisibleItemPosition()
                    val totalItemCount  = lm.itemCount

                    if (viewModel.isLoading.value == false && lastVisiblePos >= totalItemCount - 3) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }
    }

    private fun initView() {
        viewModel.error.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.isRefreshing.observe(this) { refreshing ->
            if (refreshing) listAdapter.submitList(emptyList())
            else if (viewModel.sectionsWithProducts.value?.isNotEmpty() == true) {
                binding.rvMainList.scrollToPosition(0)
            }
        }

        viewModel.sectionsWithProducts.observe(this) { list ->
            listAdapter.submitList(list)
        }
    }
}