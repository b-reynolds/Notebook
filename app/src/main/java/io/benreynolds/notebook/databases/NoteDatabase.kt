package io.benreynolds.notebook.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.benreynolds.notebook.databases.daos.NoteDao
import io.benreynolds.notebook.databases.entities.Note
import io.benreynolds.notebook.databases.typeConverters.DateTypeConverter

@Database(entities = [(Note::class)], version = 2, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "note-database"
    }

    abstract fun noteDao(): NoteDao
}
