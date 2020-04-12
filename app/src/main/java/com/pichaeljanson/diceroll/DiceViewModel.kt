package com.pichaeljanson.diceroll

import android.view.View
import androidx.lifecycle.*
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.util.concurrent.ThreadLocalRandom

class DiceViewModelFactory(private val diceSides: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiceViewModel::class.java)){
            return DiceViewModel(diceSides) as T
        }

        throw IllegalArgumentException("Unknown DiceViewModel class")
    }
}

class DiceViewModel (private var diceSides: Int) : ViewModel() {

    init{
        Timber.d("init")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
    }

    // region Dice Value

    private val _diceValue = MutableLiveData<Int>(diceSides)

    private val _isSet = MutableLiveData<Boolean>(false)

    val diceImageRes: LiveData<Int> =
        Transformations.map(_diceValue) { diceValue ->
            when (diceValue){
                1 -> R.drawable.ic_inverted_dice_1
                2 -> R.drawable.ic_inverted_dice_2
                3 -> R.drawable.ic_inverted_dice_3
                4 -> R.drawable.ic_inverted_dice_4
                5 -> R.drawable.ic_inverted_dice_5
                6 -> R.drawable.ic_inverted_dice_6
                else -> throw IndexOutOfBoundsException("Dice value unexpected: $diceValue (Dice sides: $diceSides)")
            }
        }

    val visibility: LiveData<Int> =
        Transformations.map(_isSet) { isSet ->
            if (isSet) View.VISIBLE else View.INVISIBLE
        }

    fun roll(){
        _isSet.value = true
        _diceValue.value = ThreadLocalRandom.current().nextInt(6) + 1

        vibrate()
        incrementBg()
    }

    // endregion

    // region Background

    private val _backgrounds: IntArray = intArrayOf(
        R.color.colorBackground0,
        R.color.colorBackground1,
        R.color.colorBackground2,
        R.color.colorBackground3
    )

    private val _backgroundResIndex = MutableLiveData<Int>(0)

    val backgroundRes: LiveData<Int> =
        Transformations.map(_backgroundResIndex) { bgIndex ->
            _backgrounds[bgIndex]
        }

    private fun incrementBg() {
        _backgroundResIndex.value =
            _backgroundResIndex.value
                ?.plus(1)
                ?.rem(_backgrounds.size)
    }

    // endregion

    // region Vibration

    private val _eventVibrate = MutableLiveData<VibrationType>(VibrationType.NONE)

    val eventVibrate: LiveData<VibrationType>
        get() = _eventVibrate

    private fun vibrate(){
        _eventVibrate.value = VibrationType.ROLL
    }

    fun handledVibration(){
        _eventVibrate.value = VibrationType.NONE
    }

    // endregion
}