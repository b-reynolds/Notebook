package io.benreynolds.notebook.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import io.benreynolds.notebook.databases.NoteDatabase
import io.benreynolds.notebook.R
import io.benreynolds.notebook.adapters.NoteAdapter
import io.benreynolds.notebook.viewModels.NoteDetailViewModel
import io.benreynolds.notebook.viewModels.NoteDetailViewModelFactory
import io.benreynolds.notebook.viewModels.NotebookViewModel
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.fragment_note_list.*
import timber.log.Timber

class NoteDetailFragment() : Fragment() {
    private lateinit var notesDatabase: NoteDatabase
    private lateinit var sharedViewModel: NotebookViewModel
    private lateinit var viewModel: NoteDetailViewModel

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

        viewModel.setNote(arguments?.getLong(BUNDLE_NOTE_UID)) {
            viewModel.note.value?.let {
                etTitle.setText(it.title)
                etBody.setText(it.body)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
