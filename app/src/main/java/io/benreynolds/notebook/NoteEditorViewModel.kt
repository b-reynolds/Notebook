package io.benreynolds.notebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class NoteEditorViewModel(noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()

    val editMode: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val activeNote: MutableLiveData<Note> = MutableLiveData()

    fun isValidNote(title: String, body: String): Boolean {
        return !title.isBlank()
    }

    fun loadNote(noteUid: Long, callback: (Note) -> Unit) {
        launch(UI) {
            activeNote.value = withContext(DefaultDispatcher) {
                noteDao.getNote(noteUid)
            }

            activeNote.value?.let {
                callback(it)
            }
        }
    }

    fun saveNote(title: String, body: String) {
        if (activeNote.value == null) {
            activeNote.value = Note(title = title, body = body)
        } else {
            activeNote.value?.let {
                it.title = title
                it.body = body
            }
        }

        activeNote.value?.let {
            launch {
                withContext(DefaultDispatcher) {
                    noteDao.insertAll(it)
                }
            }
        }
    }

    fun enterEditMode() {
        editMode.value = true
    }

    fun exitEditMode() {
        editMode.value = false
    }
}
