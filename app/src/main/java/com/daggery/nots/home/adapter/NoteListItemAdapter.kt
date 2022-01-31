package com.daggery.nots.home.adapter

import android.animation.TimeInterpolator
import android.annotation.SuppressLint
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
import com.daggery.nots.home.view.HomeFragmentUtils
import com.daggery.nots.utils.NotsVibrator

class NoteListItemAdapter(
    private val homeFragmentUtils: HomeFragmentUtils
) : ListAdapter<Note, NoteListItemAdapter.NoteViewHolder>(DiffCallback) {

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
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: NoteListItemAdapter.NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.listItemLayout.setOnClickListener{ homeFragmentUtils.noteClickListener(current) }
        holder.binding.listItemLayout.setOnLongClickListener {
            homeFragmentUtils.noteLongClickListener(current)
            return@setOnLongClickListener true
        }

        // TODO: Clean This Thing Up
        // TODO: Find Out Why TranslateX Works and X Don't

        var viewAnchorX: Float = 0f
        var differenceX: Float = 0f
        var swiped = false
        var translationValue = 0f
        holder.binding.listItemLayout.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    swiped = false
                    viewAnchorX = event.rawX
                    Log.d("LOL: Down", "swiped: $swiped")
                }
                MotionEvent.ACTION_UP -> {
                    // Log.d("LOL: Up", "viewAnchorX: $viewAnchorX")
                    Log.d("LOL: Up", "swiped: $swiped")
                    homeFragmentUtils.notsVibrator.resetVibrator()
                    homeFragmentUtils.isVerticalScrollActive(true)
                    v.animate()
                        .translationX(0f)
                        .setDuration(200)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                    if(swiped == false) {
                        v.performClick()
                    }
                    swiped = false
                    Log.d("LOL: Up2", "swiped: $swiped")
                }
                MotionEvent.ACTION_MOVE -> {
                    val threshold = v.width/3
                    homeFragmentUtils.isVerticalScrollActive(false)
                    translationValue = event.rawX - viewAnchorX
                    // Log.d("LOL: Translate", translationValue.toString())
                    // Log.d("LOL: Threshold", threshold.toString())
                    if(translationValue > 10 || translationValue < 10) {
                        v.translationX = (translationValue * 0.5).toFloat()
                        swiped = true
                    }
                    if(translationValue > threshold || translationValue < -threshold) {
                        homeFragmentUtils.notsVibrator.vibrate()
                    }
                }
                else -> {
                    Log.d("LOL: Else", event.action.toString())
                    swiped = false
                    homeFragmentUtils.isVerticalScrollActive(true)
                }
            }
            return@setOnTouchListener true
        }
        holder.bind(current)
    }
}