package ac.at.fhstp.note_it.feature_note.domain.model

import ac.at.fhstp.note_it.ui.theme.AdditionalColors
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Note.kt
 *
 * This data class represents a Note entity in the Room database.
 * It holds details like title, content, timestamp, color, pin status, and category.
 * The @Entity annotation makes Room create a corresponding table for it.
 */

@Entity
data class Note(
    val title: String,      // Title of the note
    val content: String,    // Main content of the note
    val timestamp: Long,    // Time the note was created/modified
    val color: Int,         // Color code representing the note’s background
    val isPinned: Boolean = false,      // Indicates if the note is pinned (default is false)
    val category: String = "All Notes", // Category to which the note belongs (default: "All Notes")

    @PrimaryKey val id: Int? = null
) {
    companion object {
        val NoteColors = AdditionalColors
    }
}

// Custom exception for handling invalid notes
class InvalidNoteException(message: String) : Exception(message)
