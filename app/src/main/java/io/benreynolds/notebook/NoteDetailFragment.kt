package io.benreynolds.notebook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
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
