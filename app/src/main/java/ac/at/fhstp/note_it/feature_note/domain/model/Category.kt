package ac.at.fhstp.note_it.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Category.kt
 *
 * Each Category has an auto-generated ID and a name.
 * The @Entity annotation tells Room to create a "categories" table for this model.
 */

// Maps this class to the "categories" table in the database
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
