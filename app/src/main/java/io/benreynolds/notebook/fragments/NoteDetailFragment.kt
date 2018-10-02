package io.benreynolds.notebook.fragments

import android.content.Context
import android.os.Bundle
import android.text.method.KeyListener
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import io.benreynolds.notebook.databases.NoteDatabase
import io.benreynolds.notebook.R
import io.benreynolds.notebook.viewModels.NoteDetailViewModel
import io.benreynolds.notebook.viewModels.NoteDetailViewModelFactory
import io.benreynolds.notebook.viewModels.NotebookViewModel
import kotlinx.android.synthetic.main.fragment_note_detail.*
import timber.log.Timber

class NoteDetailFragment() : Fragment() {
    private lateinit var notesDatabase: NoteDatabase

    private lateinit var sharedViewModel: NotebookViewModel
    private lateinit var viewModel: NoteDetailViewModel

    private lateinit var titleKeyListener: KeyListener
    private lateinit var bodyKeyListener: KeyListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        with(context as FragmentActivity) {
            initializeDatabase(this)
            initializeViewModels(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        val selectedNote = arguments?.get(BUNDLE_NOTE_UID) as Long?

        viewModel.setNote(selectedNote) {
            viewModel.note.value?.let {
                etTitle.setText(it.title)
                etBody.setText(it.body)
            }
        }

        viewModel.editMode.value = (selectedNote == null)
        viewModel.editMode.observe(this, Observer {
            it?.let {
                editMode -> toggleEditMode(editMode)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        titleKeyListener = etTitle.keyListener
        bodyKeyListener = etBody.keyListener

        viewModel.editMode.value?.let { isInEditMode ->
            updateActionButtonState(isInEditMode)
        }

        fbAction.setOnClickListener {
            viewModel.editMode.value?.let {
                val newState = !it
                viewModel.editMode.value = newState
                updateActionButtonState(newState)
            }
        }
    }

    private fun updateActionButtonState(editMode: Boolean) {
        fbAction.setImageResource(if (editMode) R.drawable.ic_save_white_24dp else R.drawable.ic_mode_edit_white_24dp)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.mbDone -> {
                viewModel.saveNote(etTitle.text.toString(), etBody.text.toString())
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun toggleEditMode(editMode: Boolean) {
        etTitle.keyListener = if (editMode) titleKeyListener else null
        etBody.keyListener = if (editMode) bodyKeyListener else null
    }

    private fun initializeDatabase(activity: FragmentActivity) {
        Timber.d("Initialising database...")
        notesDatabase = Room.databaseBuilder(
            activity.applicationContext,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    private fun initializeViewModels(activity: FragmentActivity) {
        Timber.d("Initialising shared view model...")
        sharedViewModel = ViewModelProviders.of(activity)
            .get(NotebookViewModel::class.java)

        Timber.d("Initialising view model...")
        viewModel = ViewModelProviders.of(this, NoteDetailViewModelFactory(notesDatabase))
            .get(NoteDetailViewModel::class.java)
    }

}
