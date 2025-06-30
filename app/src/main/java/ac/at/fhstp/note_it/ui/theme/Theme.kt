package ac.at.fhstp.note_it.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define a dark color palette for the app's dark mode
private val DarkColorPalette = darkColors(
    primary = Color.White,
    background = DarkGray,
    onBackground = Color.White,
    surface = LightBlue,
    onSurface = DarkGray
)

// Define a light color palette for the app's light mode
private val LightColorPalette = lightColors(
    primary = Color.Black, // Hauptfarbe in Schwarz für Light Mode
    background = Color.White, // Weißer Hintergrund für Light Mode
    onBackground = Color.Black, // Schwarzer Text
    surface = Color.LightGray, // Karten-Hintergrund in Hellgrau
    onSurface = Color.Black // Text auf Karten in Schwarz
)

// Theme composable to apply color palettes, typography, and shapes
@Composable
fun NoteItTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}