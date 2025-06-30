package ac.at.fhstp.note_it.feature_note.domain.repository

import ac.at.fhstp.note_it.feature_note.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    // Retrieves a list of all categories as a Flow
    fun getCategories(): Flow<List<Category>>

    // Inserts a new category into the database
    suspend fun insertCategory(category: Category)

    // Deletes the specified category from the database
    suspend fun deleteCategory(category: Category)
}
