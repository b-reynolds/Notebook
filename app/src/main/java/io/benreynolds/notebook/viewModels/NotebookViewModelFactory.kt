package io.benreynolds.notebook.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.benreynolds.notebook.databases.NoteDatabase

class NotebookViewModelFactory(private var notesDatabase: NoteDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotebookViewModel(notesDatabase) as T
    }
}
