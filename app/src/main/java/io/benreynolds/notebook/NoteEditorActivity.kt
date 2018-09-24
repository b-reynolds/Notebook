package io.benreynolds.notebook

import android.os.Bundle
import android.text.InputType
import android.widget.Button
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

        viewModel.editMode.observe(this, Observer { editMode ->
            etTitle.inputType = if (editMode) InputType.TYPE_TEXT_VARIATION_NORMAL else InputType.TYPE_NULL
            etBody.inputType = if (editMode) InputType.TYPE_TEXT_FLAG_MULTI_LINE else InputType.TYPE_NULL
        })

        button.setOnClickListener {
            viewModel.editMode.value?.let { currentValue ->
                viewModel.editMode.value = !currentValue
            }

            (it as Button).text = viewModel.editMode.value.toString()
        }

//        if (intent.hasExtra(EXTRA_NOTE_UID)) {
//            val noteUid = intent.getLongExtra(EXTRA_NOTE_UID, -1)
//            viewModel.loadNote(noteUid, tvTitle, tvBody)
//        }
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