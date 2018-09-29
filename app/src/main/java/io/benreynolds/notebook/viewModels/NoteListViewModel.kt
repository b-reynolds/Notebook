package io.benreynolds.notebook.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.benreynolds.notebook.databases.entities.Note
import io.benreynolds.notebook.databases.daos.NoteDao
import io.benreynolds.notebook.databases.NoteDatabase
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import timber.log.Timber

class NoteListViewModel(noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
    val notes: MutableLiveData<List<Note>> = MutableLiveData()

    fun loadNotes() {
        launch(UI) {
            Timber.d("Loading notes from the database...")
            val loadedNotes = withContext(DefaultDispatcher) {
                noteDao.getAll()
            }

            Timber.d("Updating live data with loaded notes...")
            notes.value = loadedNotes
        }
    }
}