package io.benreynolds.notebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NoteDetailViewModelFactory(private var notesDatabase: NoteDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteDetailViewModel(notesDatabase) as T
    }
}
