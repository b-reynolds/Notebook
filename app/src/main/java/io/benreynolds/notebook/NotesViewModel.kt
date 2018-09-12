package io.benreynolds.notebook

import android.arch.lifecycle.ViewModel

class NotesViewModel(private var noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
}
