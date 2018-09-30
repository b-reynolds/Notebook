package io.benreynolds.notebook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.benreynolds.notebook.R
import io.benreynolds.notebook.databases.entities.Note
import kotlinx.android.synthetic.main.item_note.view.*
import timber.log.Timber
import java.text.SimpleDateFormat

class NoteAdapter(
    val items: MutableList<Note>,
    val context: Context,
    private val onNoteClicked: ((Note) -> Unit)? = null
) : RecyclerView.Adapter<ViewHolder>() {
    init {
        setHasStableIds(true)
    }

    var sortMode: SortMode = SortMode.LAST_MODIFIED
        set(value) {
            field = value
            updateSortOrder()
        }

    fun addNotes(notes: List<Note>) {
        Timber.d("addNotes called with '%d' items, '%d' currently in list.", notes.count(), items.count())

        val notesToAdd = notes.filter { !items.contains(it) }
        if (notesToAdd.isEmpty()) {
            Timber.d("Ignoring addNotes call, no new/updated notes were found.")
            return
        }

        Timber.d("Adding/replacing '%d' notes...", notesToAdd.count())
        items.apply {
            removeAll { item ->
                notesToAdd.find { it.uid == item.uid } != null
            }
            addAll(notesToAdd)
        }

        updateSortOrder()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = items[position].title
        holder.tvBody.text = items[position].body
        holder.tvDateCreated.text = SimpleDateFormat("dd/MM/yyyy").format(items[position].dateCreated)

        holder.clRoot.setOnClickListener {
            onNoteClicked?.invoke(items[position])
        }
    }

    override fun getItemId(position: Int) = items[position].uid ?: RecyclerView.NO_ID
    override fun getItemCount() = items.size

    private fun updateSortOrder() {
        Timber.d("Sorting notes... (sortMode: %s)", sortMode.name)
        when (sortMode) {
            SortMode.ALPHABETICAL -> items.sortBy { it.title.capitalize() }
            SortMode.DATE_CREATED -> items.sortBy { it.dateCreated }
            SortMode.LAST_MODIFIED -> items.sortByDescending { it.lastModified }
        }

        Timber.d("Notifying observers...")
        notifyDataSetChanged()
    }

    enum class SortMode {
        ALPHABETICAL,
        DATE_CREATED,
        LAST_MODIFIED
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val clRoot: ConstraintLayout = view.clRoot
    val tvTitle: TextView = view.tvTitle
    val tvBody: TextView = view.tvBody
    val tvDateCreated: TextView = view.tvDateCreated
}
