package io.benreynolds.notebook

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

@Database(entities = [(Note::class)], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "note-database"
    }

    abstract fun noteDao(): NoteDao
}
