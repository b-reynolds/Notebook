package io.benreynolds.notebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.tvBody
import kotlinx.android.synthetic.main.item_note.view.tvTitle
import timber.log.Timber

class NoteAdapter(
    val items: MutableList<Note>,
    val context: Context
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = items[position].title
        holder.tvBody.text = items[position].body
    }

    override fun getItemCount() = items.size

    fun addNotes(notes: List<Note>) {
        Timber.d("addNotes called with '%d' items, '%d' currently in list.", notes.count(), items.count())

        val notesToAdd = notes.filter { note ->
            items.find { it.uid == note.uid } == null
        }

        if (notesToAdd.isEmpty()) {
            Timber.d("Ignoring addNotes call, no new notes found.")
        } else {
            Timber.d("Adding '%d' notes: '%s'", notesToAdd.count(), notesToAdd.joinToString())
            items.addAll(notesToAdd)

            Timber.d("Calling notifyDataSetChanged...")
            notifyDataSetChanged()
        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle: TextView = view.tvTitle
    val tvBody: TextView = view.tvBody
}
