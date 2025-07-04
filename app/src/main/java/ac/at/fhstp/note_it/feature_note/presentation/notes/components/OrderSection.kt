package ac.at.fhstp.note_it.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ac.at.fhstp.note_it.feature_note.domain.util.NoteOrder
import ac.at.fhstp.note_it.feature_note.domain.util.OrderType

// Composable function for displaying sorting options for notes
@Composable
fun OrderSection(
    modifier: Modifier = Modifier, // Modifier for customizing layout
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending), // Default sorting order
    onOrderChange: (NoteOrder) -> Unit // Callback when the sorting order changes
) {
    // Column layout to arrange sorting options vertically
    Column(
        modifier = modifier
    ) {
        // Row for selecting sorting criteria: Title, Date, or Color
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Radio button for sorting by title
            DefaultRadioButton(
                text = "Title",
                selected = noteOrder is NoteOrder.Title,
                onSelect = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp)) // Spacer for padding
            // Radio button for sorting by date
            DefaultRadioButton(
                text = "Date",
                selected = noteOrder is NoteOrder.Date,
                onSelect = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp)) // Spacer for padding
            // Radio button for sorting by color
            DefaultRadioButton(
                text = "Color",
                selected = noteOrder is NoteOrder.Color,
                onSelect = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp)) // Spacer for padding between sections

        // Row for selecting order type: Ascending or Descending
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Radio button for ascending order
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(noteOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp)) // Spacer for padding
            // Radio button for descending order
            DefaultRadioButton(
                text = "Descending",
                selected = noteOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(noteOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}