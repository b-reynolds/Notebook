package io.benreynolds.notebook

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class NotesViewModelFactory(private var notesDatabase: NoteDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotesViewModel(notesDatabase) as T
    }
}
