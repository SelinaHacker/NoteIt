package ac.at.fhstp.note_it.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ac.at.fhstp.note_it.feature_note.presentation.add_edit_note.AddEditNoteScreen
import ac.at.fhstp.note_it.feature_note.presentation.category.AddCategoryScreen
import ac.at.fhstp.note_it.feature_note.presentation.category.CategoryViewModel
import ac.at.fhstp.note_it.feature_note.presentation.notes.NotesScreen
import ac.at.fhstp.note_it.feature_note.presentation.util.Screen
import ac.at.fhstp.note_it.ui.theme.NoteItTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteItTheme { // Apply the custom app theme
                Surface(color = MaterialTheme.colors.background) { // Base container for the UI
                    val navController = rememberNavController() // Controls navigation between screens
                    val categoryViewModel: CategoryViewModel = viewModel() // Shared ViewModel

                    // Navigation graph setup
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route  // Starting screen
                    ) {
                        // Notes Screen
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController, categoryViewModel = categoryViewModel)
                        }

                        // Add Category Screen
                        composable(route = Screen.AddCategoryScreen.route) {
                            AddCategoryScreen(navController = navController, viewModel = categoryViewModel)
                        }

                        // Add/Edit Note Screen with optional arguments
                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                    "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(name = "noteId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(name = "noteColor") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = color,
                                categoryViewModel = categoryViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
