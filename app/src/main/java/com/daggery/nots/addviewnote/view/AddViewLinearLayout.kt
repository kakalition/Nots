package com.daggery.nots.addviewnote.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.widget.LinearLayout
import com.daggery.nots.addviewnote.utils.AddViewNoteFragmentUtils

class AddViewLinearLayout(ctx: Context, attrs: AttributeSet) : LinearLayout(ctx, attrs) {
    private var fragmentUtils: AddViewNoteFragmentUtils? = null

    fun setFragmentUtils(utils: AddViewNoteFragmentUtils) {
        fragmentUtils = utils
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent?): Boolean {
        if(event?.action == KeyEvent.ACTION_UP) {
            fragmentUtils?.hideKeyboard(this)
            fragmentUtils?.clearNoteTypingFocus()
        }
        return super.dispatchKeyEventPreIme(event)
    }
}