package ac.at.fhstp.note_it.feature_note.presentation.add_edit_note

// Data class to represent the state of a text field
data class NoteTextFieldState(
    val text: String = "", // The current text entered in the field
    val hint: String = "", // Placeholder text shown when the field is empty
    val isHintVisible: Boolean = true // Flag to control visibility of the hint
)
