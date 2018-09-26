package io.benreynolds.notebook

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [(Note::class)], version = 2, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "note-database"
    }

    abstract fun noteDao(): NoteDao
}
