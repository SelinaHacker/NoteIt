package ac.at.fhstp.note_it.feature_note.presentation.add_edit_note

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ac.at.fhstp.note_it.feature_note.domain.model.Note.Companion.NoteColors
import ac.at.fhstp.note_it.feature_note.presentation.add_edit_note.components.TransparentHintField
import ac.at.fhstp.note_it.feature_note.presentation.category.CategoryViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Palette
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val scaffoldState = rememberScaffoldState()

    // Background color animation for smooth transitions when changing note color
    val noteBackgroundAnimatable = remember {
        Animatable(Color(if (noteColor != -1) noteColor else viewModel.noteColor.value))
    }

    var isColorPickerVisible by remember { mutableStateOf(false) }
    val isPinned by viewModel.isPinned
    val categories by categoryViewModel.categories.collectAsState()
    var expanded by remember { mutableStateOf(false) }  // Category dropdown state

    val contentColor = if (MaterialTheme.colors.isLight) Color.Black else Color.White

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (titleState.text.isBlank() || contentState.text.isBlank()) {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Please fill in both the title and content before saving.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    } else {
                        viewModel.onEvent(AddEditNoteEvent.SaveNote(viewModel.selectedCategory.value))
                        navController.navigateUp()
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar: Back button, Category dropdown, Pin, Color picker
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Home",
                                tint = contentColor,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.width(120.dp)) {
                            OutlinedButton(
                                onClick = { expanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = contentColor
                                )
                            ) {
                                Text(
                                    text = viewModel.selectedCategory.value,
                                    style = MaterialTheme.typography.body2,
                                    maxLines = 1,
                                    color = contentColor
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Category",
                                    tint = contentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(
                                    if (MaterialTheme.colors.isLight) Color.White else Color.DarkGray
                                )
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.onEvent(AddEditNoteEvent.SelectedCategory("All Notes"))
                                        expanded = false
                                    }
                                ) {
                                    Text(
                                        text = "All Notes",
                                        color = contentColor
                                    )
                                }
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        onClick = {
                                            viewModel.onEvent(AddEditNoteEvent.SelectedCategory(category.name))
                                            expanded = false
                                        }
                                    ) {
                                        Text(
                                            text = category.name,
                                            color = contentColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Pin-Button
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(AddEditNoteEvent.TogglePin)
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = if (isPinned) "Note got unpinned" else "Note got pinned"
                                        )
                                    }
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                    contentDescription = "Pin note",
                                    tint = contentColor,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            // Color Picker
                            IconButton(
                                onClick = { isColorPickerVisible = !isColorPickerVisible },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = "Open Color Picker",
                                    tint = contentColor,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }
                    }
                }
                // Color Picker
                if (isColorPickerVisible) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NoteColors.forEach { color ->
                            val colorInt = color.toArgb()
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(color, CircleShape)
                                    .border(
                                        width = 2.dp,
                                        color = if (viewModel.noteColor.value == colorInt) Color.Black else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        scope.launch {
                                            noteBackgroundAnimatable.animateTo(
                                                targetValue = Color(colorInt),
                                                animationSpec = tween(durationMillis = 500)
                                            )
                                        }
                                        viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                        isColorPickerVisible = false
                                    }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TransparentHintField(
                        text = titleState.text,
                        hint = titleState.hint,
                        onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
                        isHintVisible = titleState.isHintVisible,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth(0.85f),
                        alignCenter = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(2.dp)
                    ) {
                        drawLine(
                            color = Color.Gray,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 4f
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TransparentHintField(
                        text = contentState.text,
                        hint = contentState.hint,
                        onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) },
                        isHintVisible = contentState.isHintVisible,
                        singleLine = false,
                        textStyle = MaterialTheme.typography.body1.copy(
                            fontSize = MaterialTheme.typography.body1.fontSize * 1.2f
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        alignCenter = false
                    )
                }
            }
        }
    }
}
