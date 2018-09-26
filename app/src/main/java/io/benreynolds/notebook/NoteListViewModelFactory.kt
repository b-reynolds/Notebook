package io.benreynolds.notebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NoteListViewModelFactory(private var notesDatabase: NoteDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteListViewModel(notesDatabase) as T
    }
}
