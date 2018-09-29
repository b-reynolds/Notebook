package io.benreynolds.notebook

import androidx.lifecycle.ViewModel

class NoteDetailViewModel(noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
}
