package ac.at.fhstp.note_it.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ac.at.fhstp.note_it.feature_note.domain.model.InvalidNoteException
import ac.at.fhstp.note_it.feature_note.domain.model.Note
import ac.at.fhstp.note_it.feature_note.domain.use_case.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel for managing the state and logic of the Add/Edit Note screen
@HiltViewModel
class AddEditNoteViewModel @Inject constructor(

    private val notesUseCases: NotesUseCases, // Handles note-related operations
    savedStateHandle: SavedStateHandle // Handles saved state (e.g., navigation arguments)
) : ViewModel() {

    // Note title state
    private val _noteTitle = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter title..."
        )
    )
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    // State for the note content field
    private val _noteContent = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter some content" // Default hint for the content field
        )
    )
    val noteContent: State<NoteTextFieldState> = _noteContent // Exposed immutable state

    private val _isPinned = mutableStateOf(false)
    val isPinned: State<Boolean> = _isPinned

    // State for the note color, initialized with a random color
    private val _noteColor = mutableStateOf(Note.NoteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor // Exposed immutable state

    private val _eventFlow = MutableSharedFlow<UiEvent>()

    // Holds the ID of the current note, null if creating a new note
    private var currentNoteId: Int? = null

    private val _selectedCategory = mutableStateOf("All Notes")
    val selectedCategory: State<String> = _selectedCategory

    // Initializes the ViewModel by checking if an existing note needs to be edited
    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    notesUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                        _isPinned.value = note.isPinned
                        _selectedCategory.value = note.category
                    }
                }
            }
        }
    }

    // Handles UI events triggered by user interactions
    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            // Update the title field
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            // Handle focus changes for the title field
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            // Update the content field
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            // Handle focus changes for the content field
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }
            // Update the note color
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.TogglePin -> {
                _isPinned.value = !_isPinned.value // Toggle Pin-Status
            }
            // Save the note
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        val note = Note(
                            title = noteTitle.value.text,
                            content = noteContent.value.text,
                            timestamp = System.currentTimeMillis(),
                            color = noteColor.value,
                            isPinned = isPinned.value,
                            category = event.category,
                            id = currentNoteId
                        )
                        notesUseCases.addNote(note)
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
            is AddEditNoteEvent.SelectedCategory -> {
                _selectedCategory.value = event.category
            }
        }
    }

    // UI events emitted by the ViewModel
    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent() // Event for saving a note
    }
}