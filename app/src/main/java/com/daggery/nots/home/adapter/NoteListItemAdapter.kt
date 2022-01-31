package com.daggery.nots.home.adapter

import android.animation.TimeInterpolator
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.ListItemNoteBinding

class NoteListItemAdapter(
    private val noteClickListener: (Note) -> Unit,
    private val noteLongClickListener: (Note) -> Unit,
    private val isVerticalScrollActive: (Boolean) -> Unit,
    private val vibrator: Vibrator
) : ListAdapter<Note, NoteListItemAdapter.NoteViewHolder>(DiffCallback) {

    private var shouldVibrate = true

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

        }
    }

    class NoteViewHolder(val binding: ListItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            if(note.priority == 1) {
                binding.listItemLayout.setBackgroundResource(R.drawable.list_item_note_bg_priority)
            } else binding.listItemLayout.setBackgroundResource(R.drawable.list_item_note_bg)
            binding.noteTitle.text = note.noteTitle
            binding.noteBody.text = note.noteBody
            binding.noteDate.text = note.noteDate
        }
    }

    private fun vibrate() {
        if(shouldVibrate) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, 1))
            shouldVibrate = false
        }
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
        val current = getItem(position)
        holder.itemView.setOnClickListener{ noteClickListener(current) }
        holder.itemView.setOnLongClickListener {
            noteLongClickListener(current)
            return@setOnLongClickListener true
        }

        // TODO: Clean This Thing Up
        // TODO: Find Out Why TranslateX Works and X Don't

        var viewAnchorX: Float = 0f
        var differenceX: Float = 0f
        var swiped = false
        var translationValue = 0f
        holder.itemView.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    swiped = false
                    viewAnchorX = event.rawX
                    Log.d("LOL: Down", "viewAnchorX: $viewAnchorX")
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("LOL: Up", "viewAnchorX: $viewAnchorX")
                    shouldVibrate = true
                    isVerticalScrollActive(true)
                    v.animate()
                        .translationX(0f)
                        .setDuration(200)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                    if(swiped == false) {
                        v.performClick()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    val threshold = v.width/3
                    swiped = true
                    isVerticalScrollActive(false)
                    translationValue = event.rawX - viewAnchorX
                    Log.d("LOL: Translate", translationValue.toString())
                    Log.d("LOL: Threshold", threshold.toString())
                    v.translationX = (translationValue * 0.5).toFloat()
                    if(translationValue > threshold || translationValue < -threshold) {
                        vibrate()
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    swiped = false
                    shouldVibrate = true
                    v.animate()
                        .translationX(0f)
                        .setDuration(200)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                }
                else -> {
                    isVerticalScrollActive(true)
                }
            }
            return@setOnTouchListener true
        }
        holder.bind(current)
    }
}