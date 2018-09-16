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



class NotesActivity : AppCompatActivity() {
    private lateinit var notesDatabase: NoteDatabase
    private lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDatabase()
        initializeViewModel()
        initializeRecyclerView()

        fbAdd.setOnClickListener {
            val animation1 = AlphaAnimation(1f, 0f)
            animation1.duration = 1000
            animation1.startOffset = 1000
            animation1.fillAfter = true
            fbAdd.startAnimation(animation1)
        }
    }

    private fun initializeRecyclerView() {
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.addItemDecoration(
                DividerItemDecoration(applicationContext, ClipDrawable.HORIZONTAL)
        )
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
