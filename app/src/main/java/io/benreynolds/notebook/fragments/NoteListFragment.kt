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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import io.benreynolds.notebook.databases.entities.Note
import io.benreynolds.notebook.adapters.NoteAdapter
import io.benreynolds.notebook.databases.NoteDatabase
import io.benreynolds.notebook.R
import io.benreynolds.notebook.viewModels.NoteListViewModel
import io.benreynolds.notebook.viewModels.NoteListViewModelFactory
import io.benreynolds.notebook.viewModels.NotebookViewModel
import kotlinx.android.synthetic.main.fragment_note_list.*
import timber.log.Timber

const val BUNDLE_NOTE_UID = "io.benreynolds.notebook.extras.noteUid"

class NoteListFragment : Fragment() {
    private lateinit var notesDatabase: NoteDatabase
    private lateinit var sharedViewModel: NotebookViewModel
    private lateinit var viewModel: NoteListViewModel

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initializeNoteList()
        initializeAddButton()
    }

    override fun onResume() {
        super.onResume()

        Timber.d("Requesting latest notes...")
        viewModel.loadNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mbAlphabetical -> NoteAdapter.SortMode.ALPHABETICAL
            R.id.mbDateCreated -> NoteAdapter.SortMode.DATE_CREATED
            R.id.mbLastModified -> NoteAdapter.SortMode.LAST_MODIFIED
            else -> null
        }?.let {
            with(rvNotes.adapter as NoteAdapter) {
                sortMode = it
            }

            viewModel.setSortMode(it)
        }

        return super.onContextItemSelected(item)
    }

    private fun openNoteDetailView(note: Note? = null) {
        val noteDetailFragment = NoteDetailFragment()
        noteDetailFragment.arguments = Bundle().apply {
            note?.uid?.let { putLong(BUNDLE_NOTE_UID, it) }
        }

        fbAdd.hide()

        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            ?.replace(R.id.clRoot, noteDetailFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun onNoteClicked(note: Note) {
        openNoteDetailView(note)
    }

    private fun onNotesChanged(notes: List<Note>) {
        Timber.d("Observed of notes change, updating RecyclerView adapter...")
        with(rvNotes.adapter as NoteAdapter) {
            addNotes(notes)
        }
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
        viewModel = ViewModelProviders.of(
            this,
            NoteListViewModelFactory(notesDatabase, activity.getPreferences(Context.MODE_PRIVATE))
        ).get(NoteListViewModel::class.java)
    }

    private fun initializeNoteList() {
        Timber.d("Initialising RecyclerView...")
        rvNotes.layoutManager = LinearLayoutManager(context)

        rvNotes.adapter = NoteAdapter(mutableListOf(), requireContext()) { onNoteClicked(it) }
            .apply { sortMode = viewModel.getSortMode() }

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

        rvNotes.addItemDecoration(
            DividerItemDecoration(rvNotes.context, DividerItemDecoration.VERTICAL)
        )

        Timber.d("Adding notes observer...")
        viewModel.notes.observe(this, Observer { onNotesChanged(it) })
    }

    private fun initializeAddButton() {
        fbAdd.setOnClickListener {
            openNoteDetailView()
        }
    }
}
