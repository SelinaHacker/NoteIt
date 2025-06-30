package ac.at.fhstp.note_it.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Composable function for a default styled radio button with label
@Composable
fun DefaultRadioButton(
    text: String, // Label text displayed next to the radio button
    selected: Boolean, // Indicates whether the radio button is selected
    onSelect: () -> Unit, // Callback triggered when the radio button is selected
    modifier: Modifier = Modifier // Modifier for styling and layout adjustments
) {
    // Row layout to arrange the radio button and label horizontally
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically // Aligns items vertically in the center
    ) {
        // Radio button with customizable colors
        RadioButton(
            selected = selected, // Sets the selection state
            onClick = onSelect, // Invokes the callback when clicked
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary, // Color when selected
                unselectedColor = MaterialTheme.colors.onBackground // Color when unselected
            )
        )
        Spacer(modifier = Modifier.width(8.dp)) // Spacer for padding between radio button and text
        Text(text = text, style = MaterialTheme.typography.body1) // Displays the label text
    }
}