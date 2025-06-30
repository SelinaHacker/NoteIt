package ac.at.fhstp.note_it.feature_note.presentation.add_edit_note.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

fun parseMarkdownToAnnotatedString(input: String): AnnotatedString {
    val builder = AnnotatedString.Builder()
    var index = 0

    while (index < input.length) {
        when {
            // Bold: **text**
            input.startsWith("**", index) -> {
                val endIndex = input.indexOf("**", index + 2)
                if (endIndex != -1) {
                    builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold)) // Apply bold style
                    builder.append(input.substring(index + 2, endIndex))       // Append bold text
                    builder.pop()                                              // Remove bold style
                    index = endIndex + 2                                       // Move index past closing **
                } else {
                    builder.append(input.substring(index))  // Append remaining text if no closing ** found
                    break
                }
            }
            // Italic: *text*
            input.startsWith("*", index) -> {
                val endIndex = input.indexOf("*", index + 1)
                if (endIndex != -1) {
                    builder.pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    builder.append(input.substring(index + 1, endIndex))
                    builder.pop()
                    index = endIndex + 1
                } else {
                    builder.append(input.substring(index))
                    break
                }
            }
            // Underline: _text_
            input.startsWith("_", index) -> {
                val endIndex = input.indexOf("_", index + 1)
                if (endIndex != -1) {
                    builder.pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                    builder.append(input.substring(index + 1, endIndex))
                    builder.pop()
                    index = endIndex + 1
                } else {
                    builder.append(input.substring(index))
                    break
                }
            }
            // Regular text
            else -> {
                builder.append(input[index])
                index++
            }
        }
    }
    return builder.toAnnotatedString() // Return the styled text
}
