package ac.at.fhstp.note_it.feature_note.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ac.at.fhstp.note_it.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object) interface for performing database operations on the Note entity
@Dao
interface NoteDao {

    // Query to fetch all notes as a Flow, allowing reactive data updates
    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    // Query to fetch a specific note by its ID. Returns null if the note doesn't exist
    @Query("SELECT * FROM note WHERE id= :id")
    suspend fun getNoteNyId(id: Int): Note?

    // Inserts a new note into the database or replaces it if it already exists (conflict strategy: REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    // Deletes a specific note from the database
    @Delete
    suspend fun deleteNote(note: Note)
}