package com.pichaeljanson.diceroll.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber
import java.util.concurrent.ThreadLocalRandom

class Dice(sides: Int) {
    companion object {
        val ROLLING_VALUE = 0
    }

    private val _sides = MutableLiveData<Int>(sides)

    private val _rolledValue = MutableLiveData<Int>(1)

    val rolledValue: LiveData<Int>
        get() = _rolledValue

    fun setIsRolling() {
        _rolledValue.value = ROLLING_VALUE
    }

    fun roll() {
        _rolledValue.value = ThreadLocalRandom.current().nextInt(6) + 1
    }
}