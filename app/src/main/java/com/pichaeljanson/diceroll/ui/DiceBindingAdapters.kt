package com.pichaeljanson.diceroll.ui

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.pichaeljanson.diceroll.data.Dice

@BindingAdapter("diceImage")
fun ImageView.setDiceImage(value: Int) {
    setImageResource(when (value) {
        Dice.ROLLING_VALUE -> com.pichaeljanson.diceroll.R.drawable.ic_inverted_dice_0
        1 -> com.pichaeljanson.diceroll.R.drawable.ic_inverted_dice_1
        2 -> com.pichaeljanson.diceroll.R.drawable.ic_inverted_dice_2
        3 -> com.pichaeljanson.diceroll.R.drawable.ic_inverted_dice_3
        4 -> com.pichaeljanson.diceroll.R.drawable.ic_inverted_dice_4
        5 -> com.pichaeljanson.diceroll.R.drawable.ic_inverted_dice_5
        6 -> com.pichaeljanson.diceroll.R.drawable.ic_inverted_dice_6
        else -> throw IndexOutOfBoundsException("Dice value unexpected: $value (Dice sides: 6)")
    })
}

@BindingAdapter("diceVisibility")
fun ImageView.setDiceVisibility(visibility: Int) {
    val transition = AutoTransition()
    transition.duration = 100
    transition.startDelay = 0

    TransitionManager.beginDelayedTransition(this.rootView as ViewGroup, transition)

    this.visibility = visibility
}
