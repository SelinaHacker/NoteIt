package ac.at.fhstp.note_it.feature_note.domain.use_case

import ac.at.fhstp.note_it.feature_note.domain.model.InvalidNoteException
import ac.at.fhstp.note_it.feature_note.domain.model.Note
import ac.at.fhstp.note_it.feature_note.domain.repository.NoteRepository

// Use case to add a note to the repository, validating input before insertion
class AddNote(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class) // Throws exception if note is invalid
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The Title of the note can't be empty.")

        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("The Content of the note can't be empty.")
        }
        repository.insertNote(note) // Inserts the note into the repository
    }
}