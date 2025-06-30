package ac.at.fhstp.note_it.feature_note.presentation.add_edit_note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TransparentHintField(
    text: String,
    hint: String,
    onValueChange: (String) -> Unit,
    isHintVisible: Boolean,
    singleLine: Boolean,
    textStyle: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier,
    alignCenter: Boolean = false
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (alignCenter) Alignment.Center else Alignment.TopStart
    ) {
        // Display hint when the text field is empty
        if (isHintVisible && text.isEmpty()) {
            Text(
                text = hint,
                style = textStyle.copy(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    textAlign = if (alignCenter) TextAlign.Center else TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        BasicTextField(
            value = text,
            onValueChange = { rawText ->
                onValueChange(rawText)
            },
            textStyle = textStyle.copy(
                textAlign = if (alignCenter) TextAlign.Center else TextAlign.Start
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(if (alignCenter) Alignment.Center else Alignment.TopStart),
            singleLine = singleLine,
            visualTransformation = MarkdownVisualTransformation(),
            decorationBox = { innerTextField ->
                innerTextField()
            }
        )
    }
}

class MarkdownVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val annotatedString = parseMarkdownToAnnotatedString(originalText)  // Apply Markdown formatting

        val offsetMap = mutableListOf<Int>() // Maps original text offsets to transformed text offsets
        var transformedIndex = 0

        for (i in originalText.indices) {
            if (transformedIndex < annotatedString.length &&
                annotatedString[transformedIndex] == originalText[i]
            ) {
                offsetMap.add(transformedIndex)
                transformedIndex++
            }
            else if (originalText.substring(i).startsWith("**")) {
                offsetMap.add(transformedIndex)
                continue
            }
            else if (originalText.substring(i).startsWith("*")) {
                offsetMap.add(transformedIndex)
                continue
            }
            else if (originalText.substring(i).startsWith("_")) {
                offsetMap.add(transformedIndex)
                continue
            }
            else {
                offsetMap.add(transformedIndex)
            }
        }

        // Maps the cursor position between the original and transformed text
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offsetMap.getOrElse(offset) { annotatedString.length }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return offsetMap.indexOfFirst { it >= offset }.takeIf { it != -1 } ?: originalText.length
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}
