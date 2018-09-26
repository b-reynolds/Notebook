package io.benreynolds.notebook

import android.app.Activity
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
import kotlinx.android.synthetic.main.activity_notes.*
import timber.log.Timber

class NoteListFragment() : Fragment() {
    private lateinit var viewModel: NotebookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        initializeViewModel(context as FragmentActivity)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initializeNoteList()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        (rvNotes.adapter as? NoteAdapter)?.let {
            when {
                item.itemId == R.id.mbAlphabetical -> it.sortMode = NoteAdapter.SortMode.ALPHABETICAL
                item.itemId == R.id.mbDateCreated -> it.sortMode = NoteAdapter.SortMode.DATE_CREATED
                item.itemId == R.id.mbLastModified -> it.sortMode = NoteAdapter.SortMode.LAST_MODIFIED
            }
        }

        return super.onContextItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadNotes()
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

    private fun initializeViewModel(activity: FragmentActivity) {
        viewModel = ViewModelProviders.of(activity)
            .get(NotebookViewModel::class.java)
    }
}
