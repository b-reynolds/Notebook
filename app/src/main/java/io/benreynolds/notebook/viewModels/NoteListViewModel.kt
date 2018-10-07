package io.benreynolds.notebook.viewModels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.benreynolds.notebook.adapters.NoteAdapter
import io.benreynolds.notebook.databases.entities.Note
import io.benreynolds.notebook.databases.daos.NoteDao
import io.benreynolds.notebook.databases.NoteDatabase
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import timber.log.Timber

private const val PREF_SORT_MODE = "io.benreynolds.notebook.preferences.sortMode"

class NoteListViewModel(
    noteDatabase: NoteDatabase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
    val notes: MutableLiveData<List<Note>> = MutableLiveData()

    /**
     * Sets the [NoteAdapter.SortMode] that will be should to organise notes within the application.
     * [sortMode] will be stored within the application's shared preferences so that it can be used
     * in subsequent launches.
     */
    fun setSortMode(sortMode: NoteAdapter.SortMode) {
        with(sharedPreferences.edit()) {
            putInt(PREF_SORT_MODE, sortMode.uid)
            apply()
        }

        Timber.d("Set preference '%s' to '%s'...", PREF_SORT_MODE, sortMode.name)
    }

    /**
     * Returns the [NoteAdapter.SortMode] that should be used to organise notes within the
     * application. If a [NoteAdapter.SortMode] has not yet been set (see [setSortMode]) then
     * [NoteAdapter.SortMode.DEFAULT] will be returned.
     */
    fun getSortMode(): NoteAdapter.SortMode {
        if (sharedPreferences.contains(PREF_SORT_MODE)) {
            with(sharedPreferences.all[PREF_SORT_MODE] as Int) {
                NoteAdapter.SortMode.withUid(this)?.let {
                    Timber.d(
                        "Found value for preference '%s', returning '%s'...",
                        PREF_SORT_MODE,
                        it.name
                    )

                    return it
                }
            }
        }

        Timber.d(
            "Value preference '%s' not found, returning default sort mode ('%s')...",
            PREF_SORT_MODE,
            NoteAdapter.SortMode.DEFAULT.name
        )

        return NoteAdapter.SortMode.DEFAULT
    }

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
