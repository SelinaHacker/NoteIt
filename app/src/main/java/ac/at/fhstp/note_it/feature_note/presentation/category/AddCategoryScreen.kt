package ac.at.fhstp.note_it.feature_note.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ac.at.fhstp.note_it.ui.theme.NoteItTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

/**
 * AddCategoryScreen.kt
 *
 * This screen allows the user to create a new category within the app.
 * It consists of a simple UI with:
 * - A TopAppBar containing a back button and a save button
 * - A TextField for entering the category name
 */

@Composable
fun AddCategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel
) {
    var categoryName by remember { mutableStateOf("") } // Holds user input

    NoteItTheme(darkTheme = true) {
        val backgroundColor = MaterialTheme.colors.background

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add Category", color = MaterialTheme.colors.onBackground) },
                    backgroundColor = backgroundColor,
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) { // Go back to the previous screen
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colors.onBackground)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                if (categoryName.isNotBlank()) { // Save only if input is not empty
                                    viewModel.addCategory(categoryName)
                                    navController.popBackStack()
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = "Save Category", tint = MaterialTheme.colors.onBackground)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(backgroundColor),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name", color = Color.Black) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.White,
                        textColor = Color.Black
                    )
                )
            }
        }
    }
}