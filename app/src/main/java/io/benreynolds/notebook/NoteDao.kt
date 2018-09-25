package io.benreynolds.notebook

import androidx.room.*

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
     * Deletes an existing [Note] from the database.
     */
    @Delete
    fun delete(note: Note)
}
