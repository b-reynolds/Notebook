package io.benreynolds.notebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NoteEditorViewModelFactory(private var notesDatabase: NoteDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteEditorViewModel(notesDatabase) as T
    }
}
