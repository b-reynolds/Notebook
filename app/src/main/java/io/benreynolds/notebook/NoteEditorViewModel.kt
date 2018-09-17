package io.benreynolds.notebook

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class NoteEditorViewModel(private var noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()

    fun saveNote(note: Note, onNoteSaved: (() -> Unit)? = null) {
        launch(UI) {
            withContext(DefaultDispatcher) {
                noteDao.insertAll(note)
            }

            onNoteSaved?.invoke()
        }
    }
}
