package io.benreynolds.notebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        initializeActionButton()
    }

    private fun initializeActionButton() {
        fbConfirm.setOnClickListener {
            viewModel.saveNote(Note(title = tvTitle.text.toString(), body = tvBody.text.toString())) {
                finish()
            }
        }
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