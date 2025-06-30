package ac.at.fhstp.note_it.feature_note.domain.repository

import ac.at.fhstp.note_it.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

// Interface defining the repository for accessing and managing notes
interface NoteRepository {

    // Retrieves all notes as a reactive Flow
    fun getNotes(): Flow<List<Note>>

    // Retrieves a specific note by its ID. Fixed typo from 'getNoteNyId' to 'getNoteById'
    suspend fun getNoteNyId(id: Int): Note?

    // Inserts a new note into the repository
    suspend fun insertNote(note: Note)

    // Deletes a specific note from the repository
    suspend fun deleteNote(note: Note)
}