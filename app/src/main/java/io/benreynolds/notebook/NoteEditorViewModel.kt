package io.benreynolds.notebook

import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
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

    fun loadNote(noteUid: Long, tvTitle: TextInputEditText, tvBody: TextInputEditText) {
        launch(UI) {
            var noteToView: Note? = null
            withContext(DefaultDispatcher) {
                noteToView = noteDao.getNote(noteUid)
            }

            noteToView?.let {
                tvTitle.setText(it.title)
                tvBody.setText(it.body)
            }
        }
    }
}
