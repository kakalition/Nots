package com.daggery.nots.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.ListItemNoteBinding

class NoteListItemAdapter(val noteList: List<Note>)
    : RecyclerView.Adapter<NoteListItemAdapter.NoteViewHolder>() {

    class NoteViewHolder(binding: ListItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        val noteTitle = binding.noteTitle
        val noteBody = binding.noteBody
        val noteDate = binding.noteDate
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : NoteViewHolder {
        return NoteViewHolder(ListItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: NoteListItemAdapter.NoteViewHolder, position: Int) {
        holder.noteTitle.text = noteList[position].noteTitle
        holder.noteBody.text = noteList[position].noteBody
        holder.noteDate.text = noteList[position].noteDate
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}