package io.benreynolds.notebook

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import timber.log.Timber

class NotebookActivity : AppCompatActivity() {
    private lateinit var notesDatabase: NoteDatabase
    private lateinit var viewModel: NotebookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notebook)

        initializeDatabase()
        initializeViewModel()

        addNoteListFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_notes, menu)

        return true
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
