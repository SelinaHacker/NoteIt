package ac.at.fhstp.note_it.feature_note.presentation.util

// Sealed class representing the different screens in the app's navigation
sealed class Screen(val route: String) {
    // Object representing the route for the NotesScreen
    object NotesScreen : Screen("notes_screen")
    // Object representing the route for the AddEditNoteScreen
    object AddEditNoteScreen : Screen("add_edit_note_screen")
    // Route for the Add Category screen
    object AddCategoryScreen : Screen(route = "add_category_screen")
}
