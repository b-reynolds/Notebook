package io.benreynolds.notebook

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_note_editor.*

private const val MENU_BTN_ALPHA_ENABLED = 255
private const val MENU_BTN_ALPHA_DISABLED = 130

class NoteEditorActivity : AppCompatActivity() {
    private lateinit var notesDatabase: NoteDatabase
    private lateinit var viewModel: NoteEditorViewModel

    private var doneButton: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        initializeDatabase()
        initializeViewModel()
        initializeViewMode()
        initializeEditMode()

        if (!intent.hasExtra(EXTRA_NOTE_UID)) {
            viewModel.enterEditMode()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)

        doneButton = menu.findItem(R.id.mbDone)
        val editButton = menu.findItem(R.id.mbEdit)

        viewModel.editMode.observe(this, Observer {
            doneButton?.isVisible = it
            editButton?.isVisible = !it
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mbDone) {
            viewModel.saveNote(etTitle.text.toString(), etBody.text.toString())
            synchronizeModes()
            viewModel.exitEditMode()
        } else if (item.itemId == R.id.mbEdit) {
            viewModel.enterEditMode()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateDoneButtonState() {
        doneButton?.let {
            val isValidNote = viewModel.isValidNote(etTitle.text.toString(), etBody.text.toString())

            it.isEnabled = isValidNote
            it.icon?.alpha = if (isValidNote) MENU_BTN_ALPHA_ENABLED else MENU_BTN_ALPHA_DISABLED
        }
    }

    private fun synchronizeModes() {
        tvTitle.text = etTitle.text
        tvBody.text = etBody.text
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

    private fun initializeViewMode() {
        tvBody.movementMethod = ScrollingMovementMethod()
    }

    private fun initializeEditMode() {
        viewModel.editMode.observe(this, Observer { editMode ->
            etTitle.visibility = if (editMode) EditText.VISIBLE else EditText.GONE
            etBody.visibility = if (editMode) EditText.VISIBLE else EditText.GONE
            tvTitle.visibility = if (editMode) TextView.GONE else TextView.VISIBLE
            tvBody.visibility = if (editMode) TextView.GONE else TextView.VISIBLE
        })

        updateDoneButtonState()
        etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(cs: CharSequence, s: Int, b: Int, a: Int) {}
            override fun onTextChanged(cs: CharSequence, s: Int, b: Int, a: Int) {}
            override fun afterTextChanged(editable: Editable) = updateDoneButtonState()
        })
    }
}
