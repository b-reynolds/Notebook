package io.benreynolds.notebook

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

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

    /**
     * Inserts [Note]s into the database.
     */
    @Insert
    fun insertAll(vararg notes: Note)

    /**
     * Deletes an existing [Note] from the database.
     */
    @Delete
    fun delete(note: Note)
}
