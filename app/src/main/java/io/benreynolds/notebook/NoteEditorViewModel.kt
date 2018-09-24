package io.benreynolds.notebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteEditorViewModel(private var noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()

    val editMode : MutableLiveData<Boolean> = MutableLiveData()

    init {
        editMode.value = false
    }

}
