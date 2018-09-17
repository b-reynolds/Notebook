package io.benreynolds.notebook

import android.content.Intent
import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_notes.fbAdd
import kotlinx.android.synthetic.main.activity_notes.rvNotes
import timber.log.Timber

class NotesActivity : AppCompatActivity() {
    private lateinit var notesDatabase: NoteDatabase
    private lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        initializeDatabase()
        initializeViewModel()
        initializeNoteList()
        initializeAddButton()
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadNotes()
    }

    private fun initializeAddButton() {
        Timber.d("Initializing add button...")
        fbAdd.setOnClickListener {
            Timber.d("Starting NoteEditorActivity...")
            startActivity(Intent(this, NoteEditorActivity::class.java))
        }
    }

    private fun initializeDatabase() {
        Timber.d("Initializing database...")
        notesDatabase = Room.databaseBuilder(
                application.applicationContext,
                NoteDatabase::class.java,
                NoteDatabase.DATABASE_NAME
        ).build()
    }

    private fun initializeViewModel() {
        Timber.d("Initializing view model...")
        viewModel = ViewModelProviders.of(
                this,
                NotesViewModelFactory(notesDatabase)
        )[NotesViewModel::class.java]
    }

    private fun initializeNoteList() {
        Timber.d("Initializing note list...")

        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.addItemDecoration(
            DividerItemDecoration(applicationContext, ClipDrawable.HORIZONTAL)
        )

        rvNotes.clearOnScrollListeners()
        rvNotes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, scrollState: Int) {
                when (scrollState) {
                    RecyclerView.SCROLL_STATE_IDLE -> fbAdd.show()
                    else -> fbAdd.hide()
                }

                super.onScrollStateChanged(recyclerView, scrollState)
            }
        })

        rvNotes.adapter = NoteAdapter(mutableListOf(), this) {
            Timber.d("Starting NoteEditorActivity...")
            startActivity(Intent(this, NoteEditorActivity::class.java))
        }

        viewModel.notes.observe(this, Observer {
            it?.let { notes ->
                (rvNotes.adapter as NoteAdapter).addNotes(notes)
            }
        })
    }
}
