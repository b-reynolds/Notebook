package io.benreynolds.notebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class NotesViewModel(noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
    val notes: MutableLiveData<List<Note>> = MutableLiveData()

    fun loadNotes() {
        launch(UI) {
            notes.value = withContext(DefaultDispatcher) {
                noteDao.getAll()
            }
        }
    }

}
