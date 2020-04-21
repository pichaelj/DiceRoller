package com.pichaeljanson.diceroll.ui

import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("diceImage")
fun ImageView.setDiceImage(value: Int) {
    setImageResource(when (value) {
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
    TransitionManager.beginDelayedTransition(this.rootView as ViewGroup)

    this.visibility = visibility
}
