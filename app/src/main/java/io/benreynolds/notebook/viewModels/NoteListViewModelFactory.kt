package io.benreynolds.notebook.viewModels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.benreynolds.notebook.databases.NoteDatabase

class NoteListViewModelFactory(
    private var notesDatabase: NoteDatabase,
    private var sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteListViewModel(notesDatabase, sharedPreferences) as T
    }
}
