package io.benreynolds.notebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotebookViewModelFactory(private var notesDatabase: NoteDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotebookViewModel(notesDatabase) as T
    }
}