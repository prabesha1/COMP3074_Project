package com.example.dinesmart.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = RestaurantRepository(application.applicationContext)
    private val reviewRepo = ReviewRepository(application.applicationContext)

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants

    private val _selected = MutableStateFlow<Restaurant?>(null)
    val selected: StateFlow<Restaurant?> = _selected

    // Search and filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCuisineFilter = MutableStateFlow<String?>(null)
    val selectedCuisineFilter: StateFlow<String?> = _selectedCuisineFilter

    private val _minRatingFilter = MutableStateFlow(0)
    val minRatingFilter: StateFlow<Int> = _minRatingFilter

    private val _filteredRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val filteredRestaurants: StateFlow<List<Restaurant>> = _filteredRestaurants

    // Reviews for selected restaurant
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _averageRating = MutableStateFlow(0f)
    val averageRating: StateFlow<Float> = _averageRating

    private val _reviewCount = MutableStateFlow(0)
    val reviewCount: StateFlow<Int> = _reviewCount

    init {
        viewModelScope.launch {
            repo.getRestaurants().collectLatest { list ->
                _restaurants.value = list
                applyFilters()
            }
        }

        // Combine search and filter states
        viewModelScope.launch {
            combine(
                _restaurants,
                _searchQuery,
                _selectedCuisineFilter,
                _minRatingFilter
            ) { restaurants, query, cuisine, rating ->
                filterRestaurants(restaurants, query, cuisine, rating)
            }.collectLatest { filtered ->
                _filteredRestaurants.value = filtered
            }
        }
    }

    // Search and filter functions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCuisineFilter(cuisine: String?) {
        _selectedCuisineFilter.value = cuisine
    }

    fun setMinRatingFilter(rating: Int) {
        _minRatingFilter.value = rating
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCuisineFilter.value = null
        _minRatingFilter.value = 0
    }

    private fun filterRestaurants(
        restaurants: List<Restaurant>,
        query: String,
        cuisine: String?,
        minRating: Int
    ): List<Restaurant> {
        var filtered = restaurants

        // Apply search query
        if (query.isNotBlank()) {
            filtered = filtered.filter { restaurant ->
                restaurant.name.contains(query, ignoreCase = true) ||
                restaurant.tags.contains(query, ignoreCase = true) ||
                restaurant.address.contains(query, ignoreCase = true)
            }
        }

        // Apply cuisine filter
        cuisine?.let {
            filtered = filtered.filter { restaurant ->
                restaurant.tags.contains(it, ignoreCase = true)
            }
        }

        // Apply rating filter
        if (minRating > 0) {
            filtered = filtered.filter { it.rating >= minRating }
        }

        return filtered
    }

    private fun applyFilters() {
        _filteredRestaurants.value = filterRestaurants(
            _restaurants.value,
            _searchQuery.value,
            _selectedCuisineFilter.value,
            _minRatingFilter.value
        )
    }

    // Get all unique cuisines for filter options
    fun getAllCuisines(): List<String> {
        return _restaurants.value
            .flatMap { it.tags.split(",").map { tag -> tag.trim() } }
            .distinct()
            .sorted()
    }

    fun loadById(id: Int) {
        viewModelScope.launch {
            val r = repo.getById(id)
            _selected.value = r

            // Load reviews when restaurant is selected
            r?.let { loadReviews(it.id) }
        }
    }

    fun getByIdCached(id: Int): Restaurant? = _restaurants.value.firstOrNull { it.id == id }

    fun addRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            repo.insert(restaurant)
        }
    }

    // Review management
    fun loadReviews(restaurantId: Int) {
        viewModelScope.launch {
            // Load from Firebase (real-time)
            reviewRepo.getFirebaseReviews(restaurantId).collectLatest { reviews ->
                _reviews.value = reviews
            }
        }

        viewModelScope.launch {
            _averageRating.value = reviewRepo.getAverageRating(restaurantId)
            _reviewCount.value = reviewRepo.getReviewCount(restaurantId)
        }
    }

    fun addReview(restaurantId: Int, rating: Float, comment: String, userId: String = "user_${System.currentTimeMillis()}", userName: String = "Anonymous") {
        viewModelScope.launch {
            val review = Review(
                restaurantId = restaurantId,
                userId = userId,
                userName = userName,
                rating = rating,
                comment = comment
            )
            reviewRepo.addReview(review, userId, userName)

            // Reload reviews and rating
            loadReviews(restaurantId)
        }
    }

    fun deleteReview(review: Review) {
        viewModelScope.launch {
            reviewRepo.deleteReview(review)

            _selected.value?.let { loadReviews(it.id) }
        }
    }

    fun loadSampleRestaurants() {
        viewModelScope.launch {
            repo.loadSampleRestaurants()
        }
    }

    fun deleteAllRestaurants() {
        viewModelScope.launch {
            repo.deleteAll()
        }
    }
}
