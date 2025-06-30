package ac.at.fhstp.note_it.feature_note.presentation.notes

import ac.at.fhstp.note_it.feature_note.domain.model.Note
import ac.at.fhstp.note_it.feature_note.domain.util.NoteOrder

// Sealed class representing different events in the Notes screen
sealed class NotesEvent {
    // Event for changing the order of notes
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    // Event for deleting a specific note.
    data class DeleteNote(val note: Note) : NotesEvent()
    // Event for restoring a previously deleted note
    object RestoreNote : NotesEvent()
    // Event for toggling the visibility of the order section
    object ToggleOrderSection : NotesEvent()
    // Event for search
    data class UpdateSearchQuery(val query: String) : NotesEvent()
    // Event for the Pin
    data class PinNote(val note: Note) : NotesEvent()

    // Adds a new category for notes
    data class AddCategory(val category: String) : NotesEvent()

    // Filters notes based on the selected category
    data class FilterByCategory(val category: String) : NotesEvent()
}
