package io.benreynolds.notebook

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_note_editor.*

class NoteEditorActivity : AppCompatActivity() {
    private lateinit var notesDatabase: NoteDatabase
    private lateinit var viewModel: NoteEditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        initializeDatabase()
        initializeViewModel()

        tvBody.movementMethod = ScrollingMovementMethod()

        viewModel.editMode.observe(this, Observer { editMode ->
            etTitle.visibility = if (editMode) EditText.VISIBLE else EditText.GONE
            etBody.visibility = if (editMode) EditText.VISIBLE else EditText.GONE
            tvTitle.visibility = if (editMode) TextView.GONE else TextView.VISIBLE
            tvBody.visibility = if (editMode) TextView.GONE else TextView.VISIBLE
        })

        floatingActionButton.setOnClickListener {
            viewModel.editMode.value?.let { value ->
                viewModel.editMode.value = !value
            }
        }


//        if (intent.hasExtra(EXTRA_NOTE_UID)) {
//            val noteUid = intent.getLongExtra(EXTRA_NOTE_UID, -1)
//            viewModel.loadNote(noteUid, tvTitle, tvBody)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)

        val doneButton = menu.findItem(R.id.mbDone)
        val editButton = menu.findItem(R.id.mbEdit)

        viewModel.editMode.observe(this, Observer {
            doneButton?.isVisible = it
            editButton?.isVisible = !it
        })

        return true
    }

    private fun initializeDatabase() {
        notesDatabase = Room.databaseBuilder(
                application.applicationContext,
                NoteDatabase::class.java,
                NoteDatabase.DATABASE_NAME
        ).build()
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(
                this,
                NoteEditorViewModelFactory(notesDatabase)
        )[NoteEditorViewModel::class.java]
    }
}