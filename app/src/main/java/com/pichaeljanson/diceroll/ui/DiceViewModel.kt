package com.pichaeljanson.diceroll.ui

import androidx.lifecycle.*
import com.pichaeljanson.diceroll.DiceRollingViewModel
import com.pichaeljanson.diceroll.R
import com.pichaeljanson.diceroll.RollListener
import com.pichaeljanson.diceroll.data.Dice
import java.lang.IllegalArgumentException


class DiceViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiceViewModel::class.java)){
            return DiceViewModel(Dice(6)) as T
        }

        throw IllegalArgumentException("Unknown DiceViewModel class")
    }
}

class DiceViewModel (private val dice: Dice) : ViewModel() {

    fun roll() {
        dice.roll()
    }

    // region Dice Value

    val diceImageRes: LiveData<Int> =
        Transformations.map(dice.rolledValue) { diceValue ->
            when (diceValue) {
                1 -> R.drawable.ic_inverted_dice_1
                2 -> R.drawable.ic_inverted_dice_2
                3 -> R.drawable.ic_inverted_dice_3
                4 -> R.drawable.ic_inverted_dice_4
                5 -> R.drawable.ic_inverted_dice_5
                6 -> R.drawable.ic_inverted_dice_6
                else -> throw IndexOutOfBoundsException("Dice value unexpected: $diceValue (Dice sides: 6)")
            }
        }

    // endregion
}