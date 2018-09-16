package io.benreynolds.notebook

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import android.graphics.drawable.ClipDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView





class NotesActivity : AppCompatActivity() {
    private lateinit var notesDatabase: NoteDatabase
    private lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDatabase()
        initializeViewModel()
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.addItemDecoration(
                DividerItemDecoration(applicationContext, ClipDrawable.HORIZONTAL)
        )

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

        rvNotes.adapter = NoteAdapter(mutableListOf(), this)
        viewModel.notes.observe(this, Observer { it ->
            it?.let {
                (rvNotes.adapter as NoteAdapter).addItems(it)
            }
        })
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
                NotesViewModelFactory(notesDatabase)
        )[NotesViewModel::class.java]
    }
}
