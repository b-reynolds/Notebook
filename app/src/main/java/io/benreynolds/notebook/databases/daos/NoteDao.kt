package io.benreynolds.notebook.databases.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.benreynolds.notebook.databases.entities.Note

/**
 * Interface containing methods for interacting with a database containing [Note]s.
 */
@Dao
interface NoteDao {
    /**
     * Returns a [List] containing all of the [Note]s within the database.
     */
    @Query("SELECT * FROM note")
    fun getAll(): List<Note>

    @Query("SELECT * FROM note WHERE uid= :uid ")
    fun getNote(uid: Long): Note

    /**
     * Inserts [Note]s into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg notes: Note)

    /**
     * Inserts the [Note] into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note): Long

    /**
     * Deletes an existing [Note] from the database.
     */
    @Delete
    fun delete(note: Note)
}
