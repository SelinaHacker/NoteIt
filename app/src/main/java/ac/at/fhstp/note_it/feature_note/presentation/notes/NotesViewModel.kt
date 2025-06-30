package ac.at.fhstp.note_it.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ac.at.fhstp.note_it.feature_note.domain.model.Note
import ac.at.fhstp.note_it.feature_note.domain.use_case.NotesUseCases
import ac.at.fhstp.note_it.feature_note.domain.util.NoteOrder
import ac.at.fhstp.note_it.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases
) : ViewModel() {

    // Holds the current UI state
    private val _state = mutableStateOf(NotesState())
    // Exposes immutable state to the UI
    val state: State<NotesState> = _state

    // Category filter state
    private val _selectedCategoryFilter = mutableStateOf("All Notes")

    // Stores a note for undo functionality
    private var recentlyDeletedNote: Note? = null
    // Handles asynchronous note fetching
    private var getNotesJob: Job? = null
    // Search query state
    private val _searchQuery = mutableStateOf("")
    // Exposes search query to UI
    val searchQuery: State<String> = _searchQuery

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    // Handles events triggered from the UI
    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    notesUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                    filterNotes() // Refresh after deletion
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    notesUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                    filterNotes() // Refresh after restore
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NotesEvent.UpdateSearchQuery -> {
                _searchQuery.value = event.query
                filterNotes() // Apply search filter
            }
            is NotesEvent.PinNote -> {
                viewModelScope.launch {
                    val updatedNote = event.note.copy(isPinned = !event.note.isPinned)
                    notesUseCases.addNote(updatedNote) // Toggle pin and update note
                }
            }
            is NotesEvent.AddCategory -> {
                // Placeholder for category addition if needed
            }
            is NotesEvent.FilterByCategory -> {
                _selectedCategoryFilter.value = event.category
                filterNotes() // Apply category filter
            }
        }
    }

    // Fetches notes with specified ordering (title, date, or color)
    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel() // Cancel any ongoing job to prevent conflicts
        getNotesJob = notesUseCases.getNotes(noteOrder).onEach { notes ->
            val sortedNotes = notes.sortedWith(
                compareByDescending<Note> { it.isPinned } // Pinned notes first
                    .thenComparator { note1, note2 -> // Then sort based on order type
                        when (noteOrder) {
                            is NoteOrder.Title -> if (noteOrder.orderType is OrderType.Ascending)
                                note1.title.compareTo(note2.title, ignoreCase = true)
                            else note2.title.compareTo(note1.title, ignoreCase = true)

                            is NoteOrder.Date -> if (noteOrder.orderType is OrderType.Ascending)
                                note1.timestamp.compareTo(note2.timestamp)
                            else note2.timestamp.compareTo(note1.timestamp)

                            is NoteOrder.Color -> if (noteOrder.orderType is OrderType.Ascending)
                                note1.color.compareTo(note2.color)
                            else note2.color.compareTo(note1.color)
                        }
                    }
            )
            _state.value = state.value.copy(
                notes = sortedNotes,
                noteOrder = noteOrder
            )
            filterNotes() // Apply active filters after sorting
        }.launchIn(viewModelScope)
    }

    // Filters notes based on search query and selected category
    private fun filterNotes() {
        val query = _searchQuery.value.lowercase()
        val categoryFilter = _selectedCategoryFilter.value

        val filteredNotes = state.value.notes.filter { note ->
            (categoryFilter == "All Notes" || note.category == categoryFilter) &&
                    (query.isBlank() || note.title.lowercase().contains(query) || note.content.lowercase().contains(query))
        }
        _state.value = state.value.copy(filteredNotes = filteredNotes)
    }
}
