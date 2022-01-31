package com.daggery.nots.utils

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.FragmentActivity

class NotsVibrator(private val activity: FragmentActivity) {
    private var shouldVibrate = false
    private val vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun vibrate() {
        if(shouldVibrate) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, 1))
        }
    }

    fun resetVibrator() {
        shouldVibrate = true
    }
}