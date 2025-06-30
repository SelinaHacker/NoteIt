package ac.at.fhstp.note_it.di

import ac.at.fhstp.note_it.feature_note.data.data_source.CategoryDao
import android.app.Application
import androidx.room.Room
import ac.at.fhstp.note_it.feature_note.data.data_source.NoteDatabase
import ac.at.fhstp.note_it.feature_note.data.repository.CategoryRepositoryImpl
import ac.at.fhstp.note_it.feature_note.data.repository.NoteRepositoryImpl
import ac.at.fhstp.note_it.feature_note.domain.repository.CategoryRepository
import ac.at.fhstp.note_it.feature_note.domain.repository.NoteRepository
import ac.at.fhstp.note_it.feature_note.domain.use_case.AddNote
import ac.at.fhstp.note_it.feature_note.domain.use_case.DeleteNote
import ac.at.fhstp.note_it.feature_note.domain.use_case.GetNote
import ac.at.fhstp.note_it.feature_note.domain.use_case.GetNotes
import ac.at.fhstp.note_it.feature_note.domain.use_case.NotesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides a singleton instance of the NoteDatabase
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        )
            .build()
    }

        // Provides a singleton instance of the NoteRepository
        @Provides
        @Singleton
        fun provideNoteRepository(db: NoteDatabase): NoteRepository {
            return NoteRepositoryImpl(db.noteDao) // Passes the DAO from the database to the repository implementation
        }

        // Provides a singleton instance of the use cases related to notes
        @Provides
        @Singleton
        fun provideNoteUseCases(repository: NoteRepository): NotesUseCases {
            return NotesUseCases(
                getNotes = GetNotes(repository), // Use case for retrieving all notes
                deleteNote = DeleteNote(repository),  // Use case for deleting a note
                addNote = AddNote(repository), // Use case for adding a note
                getNote = GetNote(repository) // Use case for retrieving a specific note by ID
            )
        }

        // Provides an instance of CategoryDao
        @Provides
        fun provideCategoryDao(db: NoteDatabase): CategoryDao {
            return db.categoryDao()
        }

        // Provides a singleton instance of CategoryRepository
        @Provides
        @Singleton
        fun provideCategoryRepository(db: NoteDatabase): CategoryRepository {
            return CategoryRepositoryImpl(db.categoryDao())
        }
}