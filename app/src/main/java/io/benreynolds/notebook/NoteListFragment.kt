package io.benreynolds.notebook

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_notes.*
import timber.log.Timber

class NoteListFragment() : Fragment() {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initializeNoteList()
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadNotes()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        with(rvNotes.adapter as NoteAdapter) {
            when {
                item.itemId == R.id.mbAlphabetical -> sortMode = NoteAdapter.SortMode.ALPHABETICAL
                item.itemId == R.id.mbDateCreated -> sortMode = NoteAdapter.SortMode.DATE_CREATED
                item.itemId == R.id.mbLastModified -> sortMode = NoteAdapter.SortMode.LAST_MODIFIED
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun initializeNoteList() {
        Timber.d("Initialising RecyclerView...")
        rvNotes.layoutManager = LinearLayoutManager(context)
        rvNotes.addItemDecoration(DividerItemDecoration(context, ClipDrawable.HORIZONTAL))
        rvNotes.adapter = NoteAdapter(mutableListOf(), requireContext())

        viewModel.notes.observe(this, Observer {
            it?.let { notes ->
                (rvNotes.adapter as NoteAdapter).addNotes(notes)
            }
        })
    }

    private fun initializeDatabase(activity: FragmentActivity) {
        Timber.d("Initializing database...")
        notesDatabase = Room.databaseBuilder(
                activity.applicationContext,
                NoteDatabase::class.java,
                NoteDatabase.DATABASE_NAME
        ).build()
    }

    private fun initializeViewModels(activity: FragmentActivity) {
        sharedViewModel = ViewModelProviders.of(activity)
                .get(NotebookViewModel::class.java)

        viewModel = ViewModelProviders.of(this, NoteListViewModelFactory(notesDatabase))
                .get(NoteListViewModel::class.java)
    }
}
