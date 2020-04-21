package com.pichaeljanson.diceroll

import android.view.View
import androidx.lifecycle.*
import com.pichaeljanson.diceroll.data.Dice
import timber.log.Timber
import java.lang.IllegalArgumentException


class DiceRollingViewModelFactory(private var rollListener: RollListener) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiceRollingViewModel::class.java)){
            return DiceRollingViewModel(rollListener) as T
        }

        throw IllegalArgumentException("Unknown DiceViewModel class")
    }
}

interface RollListener {
    fun onRoll()
}

class DiceRollingViewModel(private var rollListener: RollListener) : ViewModel() {

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

    // region Edit Mode

    private var _editMode = MutableLiveData<Boolean>(false)

    fun toggleEditMode() {
        _editMode.value = _editMode.value?.not()
    }

    val editModeVisibility: LiveData<Int> =
        Transformations.map(_editMode) { editMode ->
            if (editMode) View.VISIBLE else View.GONE
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

    fun roll() {
        Timber.d("roll()")
        rollListener.onRoll()

        incrementBg()
        vibrate()
    }
}