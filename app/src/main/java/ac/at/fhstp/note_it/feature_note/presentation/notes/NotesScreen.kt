package ac.at.fhstp.note_it.feature_note.presentation.notes

import ac.at.fhstp.note_it.feature_note.presentation.category.CategoryViewModel
import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ac.at.fhstp.note_it.feature_note.presentation.notes.components.NoteItem
import ac.at.fhstp.note_it.feature_note.presentation.notes.components.OrderSection
import ac.at.fhstp.note_it.feature_note.presentation.util.Screen
import ac.at.fhstp.note_it.ui.theme.NoteItTheme
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    navController: NavController, // Used for navigation between screens
    viewModel: NotesViewModel = hiltViewModel(), // Injected via Hilt
    categoryViewModel: CategoryViewModel // Provides a list of categories
) {
    // Retrieve the current state from the ViewModel (includes notes, sorting, etc.)
    val state = viewModel.state.value
    // Create a scaffold state to manage Snackbar and other scaffold-related components
    val scaffoldState = rememberScaffoldState()
    // A CoroutineScope for launching asynchronous tasks (like showing a Snackbar)
    val scope = rememberCoroutineScope()
    // Boolean to control the visibility of the search bar
    var isSearchVisible by remember { mutableStateOf(false) }
    // Boolean to track if dark mode is active; rememberSaveable persists this value
    var isDarkMode by rememberSaveable { mutableStateOf(true) }
    // Boolean controlling whether the category dropdown is expanded
    var expanded by remember { mutableStateOf(false) }
    // Get the list of categories from the CategoryViewModel
    val categories by categoryViewModel.categories.collectAsState()

    // Wrap the UI in our custom theme that adapts to dark mode
    NoteItTheme(darkTheme = isDarkMode) {
        Scaffold(
            floatingActionButton = {
                // Floating Action Button to navigate to the Add/Edit Note screen
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddEditNoteScreen.route) },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
                }
            },
            scaffoldState = scaffoldState
        ) {
            // Main container: a Column filling the screen with a background color from the theme
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(16.dp)
            ) {
                // HEADER ROW: Contains header text and a row of icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Header text
                    Text(
                        text = "Your notes",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onBackground
                    )
                    // Row containing all header icons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Dark Mode Toggle Icon
                        IconButton(onClick = { isDarkMode = !isDarkMode }) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                                contentDescription = "Toggle Dark Mode",
                                tint = MaterialTheme.colors.onBackground
                            )
                        }
                        // Sort Icon: Toggles the display of the OrderSection
                        IconButton(onClick = { viewModel.onEvent(NotesEvent.ToggleOrderSection) }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Sort",
                                tint = if (MaterialTheme.colors.isLight) Color.Black else Color.White
                            )
                        }
                        // Spacer between icons
                        Spacer(modifier = Modifier.width(8.dp))
                        // Search Icon: Toggles the visibility of the search bar
                        IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colors.onBackground
                            )
                        }
                        // Filter Icon (for category selection) with dropdown
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = "Select Category",
                                    modifier = Modifier
                                        .scale(scaleX = -1f, scaleY = 1f)
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(MaterialTheme.colors.primary)
                            ) {
                                // "All Notes" option – not deletable
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.onEvent(NotesEvent.FilterByCategory("All Notes"))
                                        expanded = false
                                    },
                                    modifier = Modifier.background(MaterialTheme.colors.primary)
                                ) {
                                    Text(text = "All Notes", color = MaterialTheme.colors.onPrimary)
                                }

                                // Loop over each user-created category and display a row with the category name and a delete icon.
                                categories.forEach { category ->
                                    // Use a custom composable inside the DropdownMenuItem to separate the selection and deletion actions.
                                    DropdownMenuItem(
                                        onClick = {}, // Provide an empty lambda since we handle the click inside the Row.
                                        modifier = Modifier.background(MaterialTheme.colors.primary)
                                    ) {
                                        // Create a Row that fills the available width.
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                // Clicking on the row (except the delete icon) selects the category.
                                                .clickable {
                                                    viewModel.onEvent(NotesEvent.FilterByCategory(category.name))
                                                    expanded = false
                                                },
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Display the category name.
                                            Text(
                                                text = category.name,
                                                color = MaterialTheme.colors.onPrimary
                                            )
                                            // Delete icon button for this category.
                                            IconButton(
                                                onClick = {
                                                    scope.launch {
                                                        categoryViewModel.deleteCategory(category)
                                                        val result = scaffoldState.snackbarHostState.showSnackbar(
                                                            message = "Category deleted",
                                                            actionLabel = "Undo",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                        if (result == SnackbarResult.ActionPerformed) {
                                                            categoryViewModel.addCategory(category.name)
                                                        }
                                                    }
                                                    expanded = false
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete category",
                                                    tint = MaterialTheme.colors.onPrimary
                                                )
                                            }
                                        }
                                    }
                                }

                                // "Create new category" option – not deletable
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        navController.navigate(Screen.AddCategoryScreen.route)
                                    },
                                    modifier = Modifier.background(MaterialTheme.colors.primary)
                                ) {
                                    Text(text = "Create new category", color = MaterialTheme.colors.onPrimary)
                                }
                            }
                        }
                    }
                }


                // ORDER SECTION: Display sorting/filter options if toggled on
                if (state.isOrderSectionVisible) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = if (MaterialTheme.colors.isLight) Color.Transparent else Color.Transparent
                    ) {
                        OrderSection(
                            noteOrder = state.noteOrder,
                            onOrderChange = { newOrder ->
                                viewModel.onEvent(NotesEvent.Order(newOrder))
                            },
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // SEARCH BAR: Shown only if isSearchVisible is true
                if (isSearchVisible) {
                    // Set up TextField colors based on current theme
                    val textFieldColors = if (MaterialTheme.colors.isLight) {
                        TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    } else {
                        TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                            cursorColor = MaterialTheme.colors.primary
                        )
                    }
                    TextField(
                        value = viewModel.searchQuery.value,
                        onValueChange = { query ->
                            viewModel.onEvent(NotesEvent.UpdateSearchQuery(query))
                        },
                        placeholder = {
                            Text(
                                "Search notes...",
                                color = if (MaterialTheme.colors.isLight) Color.Black else Color.White
                            )
                        },
                        trailingIcon = {
                            // Show an "X" icon to clear the search if there is input
                            if (viewModel.searchQuery.value.isNotEmpty()) {
                                IconButton(
                                    onClick = { viewModel.onEvent(NotesEvent.UpdateSearchQuery("")) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear search",
                                        tint = if (MaterialTheme.colors.isLight) Color.Black else Color.White
                                    )
                                }
                            }
                        },
                        colors = textFieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }

                // GRID OF NOTES: Display the filtered notes in a grid layout
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Use 2 columns for the grid
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Loop over the filtered notes and display each using the NoteItem composable
                    items(state.filteredNotes) { note ->
                        NoteItem(
                            note = note,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // On click, navigate to the Add/Edit Note screen with the note's details passed as parameters
                                    navController.navigate(
                                        Screen.AddEditNoteScreen.route +
                                                "?noteId=${note.id}&noteColor=${note.color}"
                                    )
                                },
                            onDeleteClick = {
                                // When delete is clicked, trigger a delete event and show a Snackbar with an Undo option
                                viewModel.onEvent(NotesEvent.DeleteNote(note))
                                scope.launch {
                                    val result = scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Note Deleted",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.onEvent(NotesEvent.RestoreNote)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
