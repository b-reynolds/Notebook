package io.benreynolds.notebook.viewModels

import androidx.lifecycle.ViewModel
import io.benreynolds.notebook.databases.daos.NoteDao
import io.benreynolds.notebook.databases.NoteDatabase

class NotebookViewModel(noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
}
