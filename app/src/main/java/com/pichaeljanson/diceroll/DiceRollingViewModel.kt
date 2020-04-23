package com.pichaeljanson.diceroll

import android.os.Handler
import android.view.View
import androidx.lifecycle.*
import com.pichaeljanson.diceroll.utils.CombinedLiveData
import timber.log.Timber
import java.lang.IllegalArgumentException

class DiceRollingViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiceRollingViewModel::class.java)){
            return DiceRollingViewModel() as T
        }

        throw IllegalArgumentException("Unknown DiceViewModel class")
    }
}

class DiceRollingViewModel : ViewModel() {

    // region Rolling

    private var _handler = Handler()

    private var _rolling = MutableLiveData<Boolean>(false)

    val rolling: LiveData<Boolean>
        get() = _rolling

    private fun setRolling(rolling: Boolean){
        _rolling.value = rolling
    }

    // region Start Rolling Event

    private var _startRollEvent = MutableLiveData<Boolean>(false)

    val startRollEvent: LiveData<Boolean>
        get() = _startRollEvent

    fun startRollEventHandled() {
        _startRollEvent.value = false
    }

    private fun setStartRollEvent() {
        _startRollEvent.value = true
    }

    // endregion

    // region End Rolling Event

    private var _finishRollEvent = MutableLiveData<Boolean>(false)

    val finishRollEvent: LiveData<Boolean>
        get()= _finishRollEvent

    fun finishRollHandled() {
        _finishRollEvent.value = false
    }

    private fun setFinishRollEvent() {
        _finishRollEvent.value = true
    }

    // endregion

    fun startRoll() {
        Timber.d("startRoll()")
        setRolling(true)
        setStartRollEvent()

        vibrate()

        // Finish the roll later
        _handler.postDelayed({
            finishRoll()
        }, 1000)
    }

    private fun finishRoll() {
        setRolling(false)
        setFinishRollEvent()

        incrementBg()
        vibrate()
    }

    // endregion

    // region Background

    private val _backgrounds: IntArray = intArrayOf(
        R.color.colorBackground0,
        R.color.colorBackground1,
        R.color.colorBackground2,
        R.color.colorBackground3
    )

    private val _showRollingBackground: LiveData<Boolean>
        get() = _rolling

    private val _backgroundResIndex = MutableLiveData<Int>(0)

    private val _rollBackgroundRes: LiveData<Int> =
        Transformations.map(_backgroundResIndex) { bgIndex ->
            _backgrounds[bgIndex]
        }

    private fun handleBackgroundChange(rolling: Boolean?, backgroundRes: Int?): Int {
        if (rolling == null || backgroundRes == null) {
            return R.color.rollingBackground
        }

        return if (rolling) {
            R.color.rollingBackground
        } else {
            backgroundRes
        }
    }

    val backgroundRes: LiveData<Int> = CombinedLiveData<Boolean, Int, Int>(
        _showRollingBackground,
        _rollBackgroundRes,
        this::handleBackgroundChange)

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
}