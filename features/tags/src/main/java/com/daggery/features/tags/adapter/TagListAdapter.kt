package com.daggery.features.tags.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daggery.features.tags.data.NoteTagWithStatus
import com.daggery.features.tags.databinding.TileItemTagBinding
import com.google.android.material.color.MaterialColors

class TagListAdapter(private val primaryColor: Int) : ListAdapter<NoteTagWithStatus, TagListAdapter.TagListViewHolder>(TagsDiff()) {

    inner class TagListViewHolder(private val viewBinding: TileItemTagBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(noteTag: NoteTagWithStatus) {
            viewBinding.root.setOnClickListener { noteTag.onClickListener(currentList, noteTag) }
            viewBinding.root.setOnLongClickListener { noteTag.onClickListener(currentList, noteTag)
                true}
            viewBinding.tagTitle.text = noteTag.tagName
            viewBinding.circleContent.text = noteTag.tagName[0].toString()
            viewBinding.tagCount.text = "8"

            if(noteTag.isSelected) {
                viewBinding.bgCircle.background.setTint(Color.parseColor("#FFFF6961"))
                viewBinding.root.background.setTint(Color.parseColor("#1A000000"))
            } else {
                viewBinding.bgCircle.background.setTintList(null)
                viewBinding.root.background.setTintList(null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagListViewHolder {
        return TagListViewHolder(
            TileItemTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TagListViewHolder, position: Int) {
        val noteTag = getItem(position)
        holder.bind(noteTag)
    }
}

private class TagsDiff : DiffUtil.ItemCallback<NoteTagWithStatus>() {
    override fun areItemsTheSame(oldItem: NoteTagWithStatus, newItem: NoteTagWithStatus): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteTagWithStatus, newItem: NoteTagWithStatus): Boolean {
        return oldItem == newItem
    }
}
