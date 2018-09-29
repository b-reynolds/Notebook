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
        initializeNoteListFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_notes, menu)

        return true
    }

    private fun initializeNoteListFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.clRoot, NoteListFragment())
                .commit()
    }

    private fun initializeDatabase() {
        Timber.d("Initializing database...")
        notesDatabase = Room.databaseBuilder(
                applicationContext,
                NoteDatabase::class.java,
                NoteDatabase.DATABASE_NAME
        ).build()
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(
                this,
                NotebookViewModelFactory(notesDatabase)
        )[NotebookViewModel::class.java]
    }
}
