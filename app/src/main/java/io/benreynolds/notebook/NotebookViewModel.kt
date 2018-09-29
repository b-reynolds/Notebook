package io.benreynolds.notebook

import androidx.lifecycle.ViewModel

class NotebookViewModel(noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
}