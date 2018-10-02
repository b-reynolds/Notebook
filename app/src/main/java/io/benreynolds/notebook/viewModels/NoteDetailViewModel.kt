package io.benreynolds.notebook.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.benreynolds.notebook.databases.daos.NoteDao
import io.benreynolds.notebook.databases.NoteDatabase
import io.benreynolds.notebook.databases.entities.Note
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class NoteDetailViewModel(noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
    var note: MutableLiveData<Note> = MutableLiveData()

    var editMode: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun setNote(noteUid: Long? = null, onActiveNoteSet: (() -> Unit)? = null) {
        launch(UI) {
            note.value = withContext(DefaultDispatcher) {
                noteUid?.let { noteDao.getNote(it) } ?: Note()
            }

            onActiveNoteSet?.invoke()
        }
    }

    fun saveNote(title: String, body: String, onNoteSaved: (() -> Unit)? = null) {
        launch(UI) {
            withContext(DefaultDispatcher) {
                note.value?.let {
                    it.title = title
                    it.body = body

                    setNote(noteDao.insertNote(it))
                }
            }

            onNoteSaved?.invoke()
        }
    }
}
