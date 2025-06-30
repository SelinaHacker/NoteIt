package ac.at.fhstp.note_it.feature_note.data.repository

import ac.at.fhstp.note_it.feature_note.data.data_source.CategoryDao
import ac.at.fhstp.note_it.feature_note.domain.model.Category
import ac.at.fhstp.note_it.feature_note.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

/**
 * CategoryRepositoryImpl.kt
 *
 * All category-related database operations are handled here
 */

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {

    // Fetches all categories as a Flow, allowing real-time data updates
    override fun getCategories(): Flow<List<Category>> = dao.getCategories()

    // Inserts a new category into the database
    override suspend fun insertCategory(category: Category) = dao.insertCategory(category)

    // Deletes an existing category from the database
    override suspend fun deleteCategory(category: Category) = dao.deleteCategory(category)
}
