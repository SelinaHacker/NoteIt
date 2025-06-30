package ac.at.fhstp.note_it.feature_note.domain.use_case

import ac.at.fhstp.note_it.feature_note.domain.model.Note
import ac.at.fhstp.note_it.feature_note.domain.repository.NoteRepository

// Use case to delete a note from the repository
class DeleteNote(
    private val repository: NoteRepository // Injects the NoteRepository dependency
) {

    // Deletes the given note from the repository
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}