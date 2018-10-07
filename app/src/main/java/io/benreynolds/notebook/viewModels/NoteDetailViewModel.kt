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

    var mode: MutableLiveData<Mode> = MutableLiveData<Mode>().apply {
        value = Mode.VIEW
    }

    private suspend fun loadNote(uid: Long): Note {
        return withContext(DefaultDispatcher) {
            noteDao.getNote(uid)
        }
    }

    /**
     * Searches the [NoteDatabase] for a [Note] with the specified [uid] and sets it as the
     * [NoteDetailViewModel]'s active note.
     *
     * @param uid [Note.uid] of the [Note] to set.
     * @param onNoteSet Callback method to be called when the [Note] has been set.
     */
    fun setNote(uid: Long, onNoteSet: (() -> Unit)? = null) {
        launch(UI) {
            note.value = loadNote(uid)
            mode.value = Mode.VIEW

            onNoteSet?.invoke()
        }
    }

    /**
     * Creates a new [Note] for the [NoteDetailViewModel] to manage.
     */
    fun createNewNote() {
        note.value = Note()
        mode.value = Mode.EDIT
    }

    fun toggleMode() {
        when (mode.value) {
            Mode.EDIT -> mode.value = Mode.VIEW
            Mode.VIEW -> mode.value = Mode.EDIT
        }
    }

    fun isNoteValid(title: String) = title.isNotBlank()

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

    enum class Mode {
        EDIT,
        VIEW
    }
}
