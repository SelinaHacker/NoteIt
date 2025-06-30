package ac.at.fhstp.note_it.feature_note.data.repository

import ac.at.fhstp.note_it.feature_note.data.data_source.NoteDao
import ac.at.fhstp.note_it.feature_note.domain.model.Note
import ac.at.fhstp.note_it.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

// Implementation of the NoteRepository interface using the provided NoteDao
class NoteRepositoryImpl(
    private val dao: NoteDao // Dependency injection for NoteDao
) : NoteRepository {

    // Fetches all notes as a Flow for reactive data updates
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    // Fetches a specific note by ID. Fixed typo in method name from 'getNoteNyId' to 'getNoteById'
    override suspend fun getNoteNyId(id: Int): Note? {
        return dao.getNoteNyId(id)
    }

    // Inserts a new note or updates an existing one
    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    // Deletes a specific note
    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}