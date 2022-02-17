package com.daggery.nots.addviewnote.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import com.daggery.nots.addviewnote.utils.AddViewNoteFragmentUtils
import com.google.android.material.textfield.TextInputEditText

class CustomTextInputEditText(ctx: Context, attrs: AttributeSet) : TextInputEditText(ctx, attrs) {
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP) {
            return false
        }
        return super.dispatchKeyEvent(event)
    }
}