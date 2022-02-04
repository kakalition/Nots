package com.daggery.nots.utils

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.FragmentActivity

class NotsVibrator(private val activity: FragmentActivity) {
    private var shouldVibrate = true
    private val vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun vibrate() {
        // Should vibrate only once per swipe
        if(shouldVibrate) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, 1))
            shouldVibrate = false
        }
    }

    fun resetVibrator() {
        shouldVibrate = true
    }
}