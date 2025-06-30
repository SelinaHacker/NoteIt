package ac.at.fhstp.note_it.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

// Sealed class representing different events in the Add/Edit Note screen
sealed class AddEditNoteEvent {
    // Event for entering a title
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    // Event for changing the focus state of the title input
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()
    // Event for entering content
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    // Event for changing the focus state of the content input
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()
    // Event for changing the selected color of the note
    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    // Triggered when the user saves the note, including the selected category
    data class SaveNote(val category: String) : AddEditNoteEvent()
    // Triggered when the user selects a category
    data class SelectedCategory(val category: String) : AddEditNoteEvent()
    // Toggles the pinned state of the note
    object TogglePin : AddEditNoteEvent()
}