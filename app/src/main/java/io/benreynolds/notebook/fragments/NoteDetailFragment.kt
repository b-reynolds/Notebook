package io.benreynolds.notebook.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.KeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import io.benreynolds.notebook.databases.NoteDatabase
import io.benreynolds.notebook.R
import io.benreynolds.notebook.extensions.nonNullObserve
import io.benreynolds.notebook.viewModels.NoteDetailViewModel
import io.benreynolds.notebook.viewModels.NoteDetailViewModelFactory
import io.benreynolds.notebook.viewModels.NotebookViewModel
import kotlinx.android.synthetic.main.fragment_note_detail.*
import timber.log.Timber

class NoteDetailFragment : Fragment() {
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

        setNote(arguments)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        titleKeyListener = etTitle.keyListener
        bodyKeyListener = etBody.keyListener

        initializeEditTexts()
        initializeActionButton()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    private fun setNote(transactionArguments: Bundle?) {
        if (transactionArguments == null || !transactionArguments.containsKey(BUNDLE_NOTE_UID)) {
            viewModel.createNewNote()
            return
        }

        viewModel.setNote(transactionArguments.getLong(BUNDLE_NOTE_UID)) {
            viewModel.note.value?.let { note ->
                etTitle.setText(note.title)
                etBody.setText(note.body)
            }
        }
    }

    private fun updateActionButtonState() {
        viewModel.isNoteValid(etTitle.text.toString()).let {
            fbAction.isEnabled = it
            fbAction.alpha = if (it) 1.0f else 0.75f
        }

        viewModel.mode.value?.let {
            fbAction.setImageResource(
                when (it) {
                    NoteDetailViewModel.Mode.EDIT -> R.drawable.ic_save_white_24dp
                    NoteDetailViewModel.Mode.VIEW -> R.drawable.ic_mode_edit_white_24dp
                }
            )
        }
    }

    private fun initializeEditTexts() {
        viewModel.mode.nonNullObserve(this) {
            etTitle.keyListener =
                if (it == NoteDetailViewModel.Mode.EDIT) titleKeyListener else null
            etBody.keyListener =
                if (it == NoteDetailViewModel.Mode.EDIT) bodyKeyListener else null
        }

        etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateActionButtonState()
            }
        })
    }

    private fun initializeActionButton() {
        updateActionButtonState()
        viewModel.mode.nonNullObserve(this) {
            updateActionButtonState()
        }

        fbAction.setOnClickListener {
            viewModel.mode.value?.let { mode ->
                if (mode == NoteDetailViewModel.Mode.EDIT) {
                    viewModel.saveNote(etTitle.text.toString(), etBody.text.toString())
                }

                viewModel.toggleMode()
            }
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
        viewModel = ViewModelProviders.of(this, NoteDetailViewModelFactory(notesDatabase))
            .get(NoteDetailViewModel::class.java)
    }
}
