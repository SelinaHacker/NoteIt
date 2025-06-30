package ac.at.fhstp.note_it.feature_note.domain.use_case

import ac.at.fhstp.note_it.feature_note.domain.model.Note
import ac.at.fhstp.note_it.feature_note.domain.repository.NoteRepository
import ac.at.fhstp.note_it.feature_note.domain.util.NoteOrder
import ac.at.fhstp.note_it.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Use case to fetch and sort notes based on the specified order
class GetNotes(
    private val repository: NoteRepository // Injects the NoteRepository dependency
) {

    // Retrieves notes ordered by the given criteria (default: Date Descending)
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when (noteOrder.orderType) {
                // Sorting notes in ascending order based on the specified field
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                    }
                }
                // Sorting notes in descending order based on the specified field
                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}