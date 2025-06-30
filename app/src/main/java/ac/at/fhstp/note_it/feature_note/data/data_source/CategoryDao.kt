package ac.at.fhstp.note_it.feature_note.data.data_source

import androidx.room.*
import ac.at.fhstp.note_it.feature_note.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * CategoryDao.kt
 *
 * Data Access Object (DAO) for managing category-related database operations
 *
 * Defines SQL queries and maps them to Kotlin functions using Room
 *
 * Handles basic CRUD operations for the Category entity
 */

// Marks this interface as a DAO for Room
@Dao
interface CategoryDao {
    // Fetches all categories from the "categories" table as a Flow to observe changes in real-time
    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<Category>>

    // Inserts a new category. If the category already exists (same ID), it will be replaced
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    // Deletes the specified category from the database
    @Delete
    suspend fun deleteCategory(category: Category)
}
