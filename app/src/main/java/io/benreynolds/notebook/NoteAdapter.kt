package io.benreynolds.notebook

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(
    val items: MutableList<Note>,
    val context: Context,
    val onItemClicked: (Note) -> Unit
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

        holder.clRoot.setOnClickListener {
            onItemClicked(items[position])
        }
    }

    override fun getItemCount() = items.size
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val clRoot: ConstraintLayout = view.clRoot
    val tvTitle: TextView = view.tvTitle
    val tvBody: TextView = view.tvBody
}
