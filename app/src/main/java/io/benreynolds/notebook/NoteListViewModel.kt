package io.benreynolds.notebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
            Timber.d("Loading notes...")
            val loadedNotes = mutableListOf<Note>()

            withContext(DefaultDispatcher) {
                loadedNotes.addAll(noteDao.getAll())
                Timber.d("Loaded %d notes", loadedNotes.count())
            }

            notes.value = loadedNotes
        }
    }
}