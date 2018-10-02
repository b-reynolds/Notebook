package io.benreynolds.notebook.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import io.benreynolds.notebook.databases.NoteDatabase
import io.benreynolds.notebook.R
import io.benreynolds.notebook.fragments.NoteDetailFragment
import io.benreynolds.notebook.fragments.NoteListFragment
import io.benreynolds.notebook.viewModels.NotebookViewModel
import io.benreynolds.notebook.viewModels.NotebookViewModelFactory
import timber.log.Timber

class NotebookActivity : AppCompatActivity() {
    private lateinit var notesDatabase: NoteDatabase
    private lateinit var viewModel: NotebookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notebook)

        initializeDatabase()
        initializeViewModel()

        if (savedInstanceState == null) {
            addNoteListFragment()
        }
    }

    private fun addNoteListFragment() {
        Timber.d("adding NoteListFragment...")
        supportFragmentManager
                .beginTransaction()
                .add(R.id.clRoot, NoteListFragment())
                .commit()
    }

    private fun initializeDatabase() {
        Timber.d("Initialising database...")
        notesDatabase = Room.databaseBuilder(
                applicationContext,
                NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    private fun initializeViewModel() {
        Timber.d("Initialising shared view model...")
        viewModel = ViewModelProviders.of(this, NotebookViewModelFactory(notesDatabase))
                .get(NotebookViewModel::class.java)
    }
}
