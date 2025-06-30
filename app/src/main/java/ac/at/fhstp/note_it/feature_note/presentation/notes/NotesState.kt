package ac.at.fhstp.note_it.feature_note.presentation.notes

import ac.at.fhstp.note_it.feature_note.domain.model.Note
import ac.at.fhstp.note_it.feature_note.domain.util.NoteOrder
import ac.at.fhstp.note_it.feature_note.domain.util.OrderType

// Data class representing the state of the Notes screen
data class NotesState(
    val notes: List<Note> = emptyList(), // List of notes displayed on the screen
    val filteredNotes: List<Note> = emptyList(), // Filtered list of notes
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending), // Default ordering for notes
    val isOrderSectionVisible: Boolean = false // Controls visibility of the order section
)