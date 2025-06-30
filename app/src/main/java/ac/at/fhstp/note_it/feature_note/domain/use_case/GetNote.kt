package ac.at.fhstp.note_it.feature_note.domain.use_case

import ac.at.fhstp.note_it.feature_note.domain.model.Note
import ac.at.fhstp.note_it.feature_note.domain.repository.NoteRepository

// Use case to fetch a specific note by its ID
class GetNote(
    private val repository: NoteRepository // Injects the NoteRepository dependency
) {
    // Retrieves the note with the given ID from the repository
    suspend operator fun invoke(id: Int): Note? {

        return repository.getNoteNyId(id) // Calls the repository method to fetch the note
    }
}