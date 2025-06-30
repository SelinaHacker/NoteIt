package ac.at.fhstp.note_it.feature_note.data.data_source

import ac.at.fhstp.note_it.feature_note.domain.model.Category
import androidx.room.*
import ac.at.fhstp.note_it.feature_note.domain.model.Note

/**
 * NoteDatabase.kt
 *
 * Defines the Room database for the app, which manages both notes and categories
 * It connects DAOs (Data Access Objects) to the database for performing CRUD operations
 */

@Database(
    // Tables for notes and categories
    entities = [Note::class, Category::class],
    version = 5
)
abstract class NoteDatabase : RoomDatabase() {

    // Provides access to NoteDao for note-related operations
    abstract val noteDao: NoteDao
    // Provides access to CategoryDao for category-related operations
    abstract fun categoryDao(): CategoryDao

    companion object {
        // Name of the database file
        const val DATABASE_NAME = "notes_db"
    }
}
