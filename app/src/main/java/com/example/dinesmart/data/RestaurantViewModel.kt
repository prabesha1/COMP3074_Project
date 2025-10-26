package com.example.dinesmart.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = RestaurantRepository(application.applicationContext)

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants

    private val _selected = MutableStateFlow<Restaurant?>(null)
    val selected: StateFlow<Restaurant?> = _selected

    init {
        viewModelScope.launch {
            repo.getRestaurants().collectLatest { list ->
                _restaurants.value = list
            }
        }
    }

    fun loadById(id: Int) {
        viewModelScope.launch {
            val r = repo.getById(id)
            _selected.value = r
        }
    }

    fun getByIdCached(id: Int): Restaurant? = _restaurants.value.firstOrNull { it.id == id }
}
