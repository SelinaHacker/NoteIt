package ac.at.fhstp.note_it.feature_note.domain.use_case

// Container class for grouping all note-related use cases
data class NotesUseCases(
    val getNotes: GetNotes, // Use case to fetch and sort notes
    val deleteNote: DeleteNote, // Use case to delete a note
    val addNote: AddNote, // Use case to add a note
    val getNote: GetNote // Use case to fetch a specific note by ID
)