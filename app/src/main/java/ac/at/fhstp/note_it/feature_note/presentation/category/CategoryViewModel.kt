package ac.at.fhstp.note_it.feature_note.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ac.at.fhstp.note_it.feature_note.domain.model.Category
import ac.at.fhstp.note_it.feature_note.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().collect { categoryList ->
                _categories.value = categoryList
            }
        }
    }

    fun addCategory(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                repository.insertCategory(Category(name = name))
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }
}
