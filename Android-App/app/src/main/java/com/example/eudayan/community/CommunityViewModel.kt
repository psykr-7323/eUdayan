package com.example.eudayan.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eudayan.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    private val _discussions = MutableStateFlow<List<Discussion>>(emptyList())
    val discussions = _discussions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        fetchDiscussions()
    }

    fun fetchDiscussions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _discussions.value = ApiClient.apiService.getDiscussions()
            } catch (e: Exception) {
                _error.value = "Failed to fetch posts: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}