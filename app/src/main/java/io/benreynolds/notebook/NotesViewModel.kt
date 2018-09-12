package io.benreynolds.notebook

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class NotesViewModel(private var noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
    val notes: MutableLiveData<List<Note>> = MutableLiveData()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        launch(UI) {
            val loadedNotes = mutableListOf<Note>()
            withContext(DefaultDispatcher) {
                loadedNotes.addAll(noteDao.getAll())
            }
            notes.value = loadedNotes
        }
    }
}
