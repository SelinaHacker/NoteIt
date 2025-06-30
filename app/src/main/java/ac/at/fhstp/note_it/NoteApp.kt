package ac.at.fhstp.note_it

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Application class annotated with @HiltAndroidApp to enable Hilt dependency injection
@HiltAndroidApp
class NoteApp : Application() {
    // This class initializes Hilt for dependency injection and sets up the application context
}