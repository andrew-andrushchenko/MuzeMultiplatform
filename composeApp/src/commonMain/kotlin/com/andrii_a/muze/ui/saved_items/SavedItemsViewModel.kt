package com.andrii_a.muze.ui.saved_items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrii_a.muze.domain.models.SavedItem
import com.andrii_a.muze.domain.repository.SavedItemsRepository
import com.andrii_a.muze.domain.util.SavedItemType
import com.andrii_a.muze.ui.saved_items.components.DisplayItemTypes
import com.andrii_a.muze.ui.saved_items.components.SavedItemsFilters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedItemsViewModel(
    private val savedItemsRepository: SavedItemsRepository
) : ViewModel() {

    private val _state: MutableStateFlow<SavedItemsUiState> = MutableStateFlow(SavedItemsUiState())
    val state: StateFlow<SavedItemsUiState> = _state.asStateFlow()

    init {
        onEvent(SavedItemsEvent.ChangeFilters(SavedItemsFilters()))
    }

    private val navigationChannel = Channel<SelectedItemNavigationEvent>()
    val navigationEventsFlow = navigationChannel.receiveAsFlow()

    fun onEvent(event: SavedItemsEvent) {
        when (event) {
            is SavedItemsEvent.ChangeFilters -> {
                changeFilters(event.filters)
            }

            is SavedItemsEvent.SelectItem -> {
                selectItem(event.item)
            }

            is SavedItemsEvent.RemoveItem -> {
                removeItem(event.item)
            }

            is SavedItemsEvent.RemoveAllItems -> {
                removeAllItems()
            }

            is SavedItemsEvent.OpenFilterDialog -> {
                _state.update { it.copy(isFilterDialogOpen = true) }
            }

            is SavedItemsEvent.DismissFilterDialog -> {
                _state.update { it.copy(isFilterDialogOpen = false) }
            }
        }
    }

    private fun removeAllItems() {
        viewModelScope.launch {
            withContext(NonCancellable) {
                savedItemsRepository.deleteAllItems()
            }
        }
    }

    private fun removeItem(item: SavedItem) {
        viewModelScope.launch {
            withContext(NonCancellable) {
                savedItemsRepository.deleteItem(item)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun changeFilters(filters: SavedItemsFilters) {
        _state.update { it.copy(filters = filters) }

        viewModelScope.launch {
            savedItemsRepository.getSavedItems(filters.sortOrder)
                .flatMapLatest { savedItems ->
                    flow {
                        when (filters.displayItemTypes) {
                            DisplayItemTypes.ALL -> {
                                emit(savedItems)
                            }

                            DisplayItemTypes.ARTISTS -> {
                                val filteredItems = savedItems.filter { it.type == SavedItemType.ARTIST }
                                emit(filteredItems)
                            }

                            DisplayItemTypes.ARTWORKS -> {
                                val filteredItems = savedItems.filter { it.type == SavedItemType.ARTWORK }
                                emit(filteredItems)
                            }
                        }
                    }
                }
                .collect { items ->
                    _state.update { it.copy(items = items) }
                }
        }
    }

    private fun selectItem(item: SavedItem) {
        viewModelScope.launch {
            navigationChannel.send(SelectedItemNavigationEvent.NavigateToSavedItemDetail(item))
        }
    }
}