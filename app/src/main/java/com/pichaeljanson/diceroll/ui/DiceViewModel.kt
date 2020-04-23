package com.pichaeljanson.diceroll.ui

import android.view.View
import androidx.lifecycle.*
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

    private var _dice: List<Dice> = listOf(
        Dice(6),
        Dice(6),
        Dice(6),
        Dice(6),
        Dice(6)
    )

    // region Rolling

    private var _rolling = MutableLiveData<Boolean>(false)

    fun startRoll() {
        _dice.forEach { dice -> dice.setIsRolling() }
    }

    fun roll() {
        _dice.forEach { dice -> dice.roll() }
    }

    // endregion

    // region Count

    private val MAX_DICE = 5

    private var _numberOfDice = MutableLiveData<Int>(1)

    fun incrementDice() {
        var diceCount = _numberOfDice.value

        if (diceCount != null && diceCount < MAX_DICE){
            diceCount += 1
        }

        _numberOfDice.value = diceCount

        tryChangeAddDiceEnabled()
        tryChangeRemoveDiceEnabled()
    }

    fun decrementDice() {
        var diceCount = _numberOfDice.value

        if (diceCount != null && diceCount > 1) {
            diceCount -= 1
        }

        _numberOfDice.value = diceCount

        tryChangeAddDiceEnabled()
        tryChangeRemoveDiceEnabled()
    }

    // endregion

    // region Add/Remove Dice Enabled

    private var _addDiceEnabled = true

    private var _removeDiceEnabled = false

    val addDiceEnabled: Boolean
        get() = _addDiceEnabled

    val removeDiceEnabled: Boolean
        get() = _removeDiceEnabled

    private fun tryChangeAddDiceEnabled() {
        var diceCount = _numberOfDice.value

        if (diceCount != null){
            var newEnabled = diceCount < MAX_DICE

            if (newEnabled != _addDiceEnabled) {
                _addDiceEnabled = newEnabled
                notifyEventMenuRefresh()
            }
        }
    }

    private fun tryChangeRemoveDiceEnabled() {
        var diceCount = _numberOfDice.value

        if (diceCount != null) {
            var newEnabled = diceCount > 1

            if (newEnabled != _removeDiceEnabled) {
                _removeDiceEnabled = newEnabled
                notifyEventMenuRefresh()
            }
        }
    }

    private var _eventMenuRefresh = MutableLiveData<Boolean>(false)

    private fun notifyEventMenuRefresh(){
        _eventMenuRefresh.value = true
    }

    val eventMenuRefresh: LiveData<Boolean>
        get() = _eventMenuRefresh

    fun notifyEventMenuRefreshHandled(){
        _eventMenuRefresh.value = false
    }

    // endregion

    // region Visibility

    val diceVisibility0: LiveData<Int> =
        Transformations.map(_numberOfDice) { numberOfDice ->
            diceVisibility(0, numberOfDice)
        }

    val diceVisibility1: LiveData<Int> =
        Transformations.map(_numberOfDice) { numberOfDice ->
            diceVisibility(1, numberOfDice)
        }

    val diceVisibility2: LiveData<Int> =
        Transformations.map(_numberOfDice) { numberOfDice ->
            diceVisibility(2, numberOfDice)
        }

    val diceVisibility3: LiveData<Int> =
        Transformations.map(_numberOfDice) { numberOfDice ->
            diceVisibility(3, numberOfDice)
        }

    val diceVisibility4: LiveData<Int> =
        Transformations.map(_numberOfDice) { numberOfDice ->
            diceVisibility(4, numberOfDice)
        }


    private fun diceVisibility(diceN: Int, numberOfDice: Int) : Int {
        return if (diceN < numberOfDice) View.VISIBLE else View.GONE
    }

    // endregion

    // region Image

    val dice0: LiveData<Int> =
        Transformations.map(_dice[0].rolledValue) { it }

    val dice1: LiveData<Int> =
        Transformations.map(_dice[1].rolledValue) { it }

    val dice2: LiveData<Int> =
        Transformations.map(_dice[2].rolledValue) { it }

    val dice3: LiveData<Int> =
        Transformations.map(_dice[3].rolledValue) { it }

    val dice4: LiveData<Int> =
        Transformations.map(_dice[4].rolledValue) { it }

    // endregion
}