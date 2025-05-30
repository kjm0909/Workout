package com.kurly.kjm.kurlytest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kurly.kjm.kurlytest.api.ApiService
import com.kurly.kjm.kurlytest.data.SectionProductData
import com.kurly.kjm.kurlytest.data.SectionWithProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(application: Application): AndroidViewModel(application) {
    private val apiService = ApiService(application)

    private val _sectionsWithProducts = MutableLiveData<List<SectionWithProducts>>()
    val sectionsWithProducts: LiveData<List<SectionWithProducts>> = _sectionsWithProducts

    private val _paging = MutableLiveData<Int?>()

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadNextPage() {
        viewModelScope.launch {
            val nextPage = _paging.value ?: return@launch

            if (_isLoading.value == true || _isRefreshing.value == true || _paging.value == null) return@launch
            _isLoading.value = true
            try {
                val page = withContext(Dispatchers.IO) {
                    apiService.api.loadPage(nextPage)
                }
                _paging.value = page.paging?.nextPage

                val deferred = page.sectionData.map { section ->
                    async(Dispatchers.IO) {
                        val items = apiService.api
                            .getProducts(section.id)
                            .sectionProductData
                            .map {
                            it.copy(isTextVisible = it.discountedPrice != null)
                        }
                        SectionWithProducts(section, items)
                    }
                }
                val newSections = deferred.awaitAll()
                val current = _sectionsWithProducts.value?.toMutableList() ?: mutableListOf()
                current.addAll(newSections)
                _sectionsWithProducts.postValue(current)
            }
            catch (e: Exception) {
                _error.value = e.localizedMessage
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshSections() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _paging.value = 1
            try {
                val page = withContext(Dispatchers.IO) {
                    apiService.api.loadPage(_paging.value!!)
                }
                _paging.value = page.paging?.nextPage

                val deferred = page.sectionData.map { section ->
                    async(Dispatchers.IO) {
                        val items = apiService.api.getProducts(section.id).sectionProductData.map {
                            it.copy(isTextVisible = it.discountedPrice != null)
                        }
                        SectionWithProducts(section, items)
                    }
                }
                _sectionsWithProducts.postValue(deferred.awaitAll())
            }
            catch (e: Exception) {
                _error.value = e.localizedMessage
            }
            finally {
                _isRefreshing.value = false
            }
        }
    }

    fun loadItemsForSection(entry: SectionWithProducts): List<SectionProductData> = entry.items
}