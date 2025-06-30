package ac.at.fhstp.note_it.feature_note.presentation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import ac.at.fhstp.note_it.feature_note.domain.model.Note
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.ui.text.font.FontWeight
import ac.at.fhstp.note_it.feature_note.presentation.add_edit_note.components.parseMarkdownToAnnotatedString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Formats a timestamp (in milliseconds) into a human-readable date string.
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

// Composable function to display a note item with customizable design
@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(200.dp)
            .padding(8.dp)
    ) {
        // Draws the note's background with a cut-corner effect
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            clipPath(clipPath) {
                // Main background color of the note
                drawRoundRect(
                    color = Color(note.color),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(note.color, 0x000000, 0.2f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Shows the pin icon if the note is pinned
            if (note.isPinned) {
                Icon(
                    imageVector = Icons.Filled.PushPin,
                    contentDescription = "Pinned Note",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
            // Title of the note
            Text(
                text = note.title,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Content of the note
            Text(
                text = parseMarkdownToAnnotatedString(note.content),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            // Date and Delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    // Displays formatted date
                    text = formatDate(note.timestamp),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = MaterialTheme.typography.subtitle1.fontSize * 1f,
                    ),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                IconButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete note",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}