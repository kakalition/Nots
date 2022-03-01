package com.daggery.features.noteeditor.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout

class NoteEditorLayout(ctx: Context, attrs: AttributeSet) : LinearLayout(ctx, attrs) {
    private var hideKeyboardCallback: ((View) -> Boolean)? = null
    private var clearNoteTypingFocusCallback: (() -> Unit)? = null

    fun setCallback(
        hideKeyboardCallbackValue: (View) -> Boolean,
        clearNoteTypingFocusCallbackValue: () -> Unit
    ) {
        hideKeyboardCallback = hideKeyboardCallbackValue
        clearNoteTypingFocusCallback = clearNoteTypingFocusCallbackValue
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent?): Boolean {
        if(event?.action == KeyEvent.ACTION_UP) {
            hideKeyboardCallback?.invoke(this)
            clearNoteTypingFocusCallback?.invoke()
        }
        return super.dispatchKeyEventPreIme(event)
    }
}